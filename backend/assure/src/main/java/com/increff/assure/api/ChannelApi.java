package com.increff.assure.api;

import com.increff.assure.dao.ChannelDao;
import com.increff.assure.model.form.ChannelNameUpdateForm;
import com.increff.assure.pojo.ChannelPojo;
import com.increff.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ChannelApi {

    public static final String INTERNAL_CHANNEL_NAME = "internal";

    @Autowired
    private ChannelDao channelDao;

    public void add(ChannelPojo channelPojo) {
        ChannelPojo exists = channelDao.select(channelPojo.getName());
        if (!isNull(exists)) {
            return;
        }
        channelDao.insert(channelPojo);
    }

    @Transactional(readOnly = true)
    public ChannelPojo get(Long channelId) throws ApiException {
        return channelDao.select(channelId);
    }

    @Transactional(readOnly = true)
    public ChannelPojo getByChannelName(String channelName) {
        return channelDao.select(channelName);
    }

    @Transactional(readOnly = true)
    public ChannelPojo getInternalChannelInfo() {
        return channelDao.select(INTERNAL_CHANNEL_NAME);
    }

    @Transactional(readOnly = true)
    public void throwsIfInternalChannelNotExist() throws ApiException {
        if (isNull(getInternalChannelInfo())) {
            throw new ApiException("internal channel does not exist");
        }
    }

    @Transactional(readOnly = true)
    public void throwsIfInvalidChannelId(Long channelId, boolean internalOrder) throws ApiException {
        if (isNull(get(channelId))) {
            throw new ApiException("invalid channel id: " + channelId);
        }
        if (internalOrder) {
            if (!getInternalChannelInfo().getId().equals(channelId)) {
                throw new ApiException("channel id is wrong for internal order");
            }
        } else {
            if (getInternalChannelInfo().getId().equals(channelId)) {
                throw new ApiException("channel id is invalid for channel order");
            }
        }
    }

    public void update(ChannelNameUpdateForm channelNameUpdateForm) throws ApiException {
        ChannelPojo channelPojo = channelDao.select(channelNameUpdateForm.getOldName());
        if (isNull(channelPojo)) {
            throw new ApiException("no channel found with name: " + channelNameUpdateForm.getOldName());
        }
        channelPojo.setName(channelNameUpdateForm.getNewName());
    }
}
