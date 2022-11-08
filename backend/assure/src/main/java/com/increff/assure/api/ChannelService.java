package com.increff.assure.api;

import com.increff.assure.dao.ChannelDao;
import com.increff.assure.exception.ApiException;
import com.increff.assure.pojo.ChannelPojo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChannelService {

    @Autowired
    private ChannelDao dao;

    @Transactional
    public void add(ChannelPojo channelPojo) {
        dao.insert(channelPojo);
    }

    @Transactional(readOnly = true)
    public ChannelPojo get(Long channelId) throws ApiException {
        return dao.select(channelId);
    }
}
