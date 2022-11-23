package com.increff.assure.dao;

import com.increff.assure.pojo.OrderItemPojo;
import com.increff.commons.exception.ApiException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional(rollbackFor = ApiException.class)
public class OrderItemDao extends AbstractDao {

    public static final String SELECT_BY_ORDERID_GLOBALSKUID = "select o from OrderItemPojo o " +
            "where orderId=:orderId and globalSkuId=:globalSkuId";

    public static final String SELECT_BY_ORDERID = "select o from OrderItemPojo o " +
            "where orderId=:orderId";

    public void insert(OrderItemPojo orderItemPojo) {
        em().persist(orderItemPojo);
    }

    @Transactional(readOnly = true)
    public OrderItemPojo select(Long orderId, Long globalSkuId) {
        TypedQuery<OrderItemPojo> query = em().createQuery(
                SELECT_BY_ORDERID_GLOBALSKUID,
                OrderItemPojo.class
        );
        query.setParameter("orderId", orderId);
        query.setParameter("globalSkuId", globalSkuId);
        return getSingle(query);
    }

    @Transactional(readOnly = true)
    public List<OrderItemPojo> select(Long orderId) {
        TypedQuery<OrderItemPojo> query = em().createQuery(
                SELECT_BY_ORDERID,
                OrderItemPojo.class
        );
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }
}
