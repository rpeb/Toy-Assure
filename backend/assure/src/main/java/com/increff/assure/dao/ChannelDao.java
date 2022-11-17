package com.increff.assure.dao;

import com.increff.assure.pojo.ChannelPojo;
import com.increff.commons.exception.ApiException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;

@Repository
@Transactional(rollbackFor = ApiException.class)
public class ChannelDao extends AbstractDao {

    public static final String SELECT_BY_ID = "select c from ChannelPojo c " +
            "where id=:channelId";

    public static final String SELECT_BY_NAME = "select c from ChannelPojo c " +
            "where name=:name";

    public void insert(ChannelPojo channelPojo) {
        em().persist(channelPojo);
    }

    @Transactional(readOnly = true)
    public ChannelPojo select(Long channelId) {
        TypedQuery<ChannelPojo> query = em().createQuery(SELECT_BY_ID, ChannelPojo.class);
        query.setParameter("channelId", channelId);
        return getSingle(query);
    }

    @Transactional(readOnly = true)
    public ChannelPojo select(String channelName) {
        TypedQuery<ChannelPojo> query = em().createQuery(SELECT_BY_ID, ChannelPojo.class);
        query.setParameter("name", channelName);
        return getSingle(query);
    }
}
