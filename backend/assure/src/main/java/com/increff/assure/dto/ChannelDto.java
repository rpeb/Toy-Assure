package com.increff.assure.dto;

import com.increff.assure.api.ChannelApi;
import com.increff.assure.dto.helper.ChannelDtoHelper;
import com.increff.assure.model.form.ChannelForm;
import com.increff.assure.model.form.ChannelNameUpdateForm;
import com.increff.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ChannelDto {

    @Autowired
    private ChannelApi channelApi;

    public void add(ChannelForm channelForm) throws ApiException {
        ChannelDtoHelper.normalize(channelForm);
        channelApi.add(ChannelDtoHelper.convertChannelFormToChannelPojo(channelForm));
    }

    public void updateName(ChannelNameUpdateForm channelNameUpdateForm) throws ApiException {
        ChannelDtoHelper.normalizeChannelNameUpdateForm(channelNameUpdateForm);
        channelApi.update(channelNameUpdateForm);
    }
}
