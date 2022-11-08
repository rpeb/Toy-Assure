package com.increff.assure.dao;

import com.increff.assure.pojo.ChannelListingPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ChannelListingDao extends AbstractDao {

    @Transactional
    public void insert(ChannelListingPojo channelListingPojo) {
        em().persist(channelListingPojo);
    }
}
