package com.increff.assure.dao;

import com.increff.assure.pojo.ChannelPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;

@Repository
public class ChannelDao extends AbstractDao {

    public static final String SELECT_BY_ID = "select c from ChannelPojo c " +
            "where id=:channelId";

    @Transactional
    public void insert(ChannelPojo channelPojo) {
        em().persist(channelPojo);
    }

    @Transactional(readOnly = true)
    public ChannelPojo select(Long channelId) {
        TypedQuery<ChannelPojo> query = em().createQuery(SELECT_BY_ID, ChannelPojo.class);
        query.setParameter("channelId", channelId);
        return getSingle(query);
    }
}
