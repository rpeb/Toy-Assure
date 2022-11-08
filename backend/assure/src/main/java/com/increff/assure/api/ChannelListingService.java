package com.increff.assure.api;

import com.increff.assure.dao.ChannelListingDao;
import com.increff.assure.exception.ApiException;
import com.increff.assure.pojo.ChannelListingPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChannelListingService {
    @Autowired
    private ChannelListingDao dao;

    @Transactional(rollbackFor = ApiException.class)
    public void add(List<ChannelListingPojo> channelListingPojos) {
        channelListingPojos.forEach(channelListingPojo -> dao.insert(channelListingPojo));
    }
}
