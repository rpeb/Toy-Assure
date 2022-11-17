package com.increff.assure.dao;

import com.increff.assure.pojo.OrderPojo;
import com.increff.commons.exception.ApiException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;

@Repository
@Transactional(rollbackFor = ApiException.class)
public class OrderDao extends AbstractDao {

    public static final String SELECT_BY_CLIENTID_CHANNELID_CHANNELORDERID = "select o from OrderPojo o " +
            "where clientId=:clientId and channelId=:channelId and channelOrderId=:channelOrderId";

    public static final String SELECT_BY_CHANNELORDERID = "select o from OrderPojo o " +
            "where channelOrderId=:channelOrderId";

    public static final String SELECT_BY_ORDERID = "select o from OrderPojo o where id=:id";

    public OrderPojo insert(OrderPojo orderPojo) {
        em().persist(orderPojo);
        return orderPojo;
    }

    @Transactional(readOnly = true)
    public OrderPojo select(Long clientId, Long channelId, String channelOrderId) {
        TypedQuery<OrderPojo> query = em().createQuery(
                SELECT_BY_CLIENTID_CHANNELID_CHANNELORDERID,
                OrderPojo.class
        );
        query.setParameter("clientId", clientId);
        query.setParameter("channelId", channelId);
        query.setParameter("channelOrderId", channelOrderId);
        return getSingle(query);
    }

    @Transactional(readOnly = true)
    public OrderPojo select(String channelOrderId) {
        TypedQuery<OrderPojo> query = em().createQuery(
                SELECT_BY_CHANNELORDERID,
                OrderPojo.class
        );
        query.setParameter("channelOrderId", channelOrderId);
        return getSingle(query);
    }

    @Transactional(readOnly = true)
    public OrderPojo select(Long orderId) {
        TypedQuery<OrderPojo> query = em().createQuery(
                SELECT_BY_ORDERID,
                OrderPojo.class
        );
        query.setParameter("id", orderId);
        return getSingle(query);
    }

    public void update(Long orderId, String url) {
        OrderPojo pojo = select(orderId);
        pojo.setInvoiceUrl(url);
    }
}
