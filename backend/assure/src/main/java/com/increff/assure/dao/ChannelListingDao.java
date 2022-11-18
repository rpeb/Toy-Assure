package com.increff.assure.dao;

import com.increff.assure.pojo.ChannelListingPojo;
import com.increff.commons.exception.ApiException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;

@Repository
@Transactional(rollbackFor = ApiException.class)
public class ChannelListingDao extends AbstractDao {

    public static final String SELECT_BY_CHANNELID_CHANNELSKU_CLIENTID = "select c from ChannelListingPojo c " +
            "where channelId=:channelId and channelSkuId=:channelSkuId and clientId=:clientId";

    public void insert(ChannelListingPojo channelListingPojo) {
        em().persist(channelListingPojo);
    }

    @Transactional(readOnly = true)
    public ChannelListingPojo selectByClientIdChannelIdChannelSkuId(Long clientId, Long channelId, String channelSkuId) {
        TypedQuery<ChannelListingPojo> query = em().createQuery(
                SELECT_BY_CHANNELID_CHANNELSKU_CLIENTID,
                ChannelListingPojo.class
        );
        query.setParameter("clientId", clientId);
        query.setParameter("channelId", channelId);
        query.setParameter("channelSkuId", channelSkuId);
        return getSingle(query);
    }
}
