package com.increff.assure.api;

import com.increff.assure.dao.OrderDao;
import com.increff.assure.model.data.OrderData;
import com.increff.assure.pojo.OrderPojo;
import com.increff.assure.pojo.OrderStatus;
import com.increff.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderApi {

    @Autowired
    private OrderDao orderDao;

    public OrderPojo createOrder(OrderPojo orderPojo) throws ApiException {
        boolean found = checkIfOrderWithSameUniqueConstraintsFound(orderPojo);
        if (!found) {
            return orderDao.insert(orderPojo);
        }
        Long clientId = orderPojo.getClientId();
        Long channelId = orderPojo.getChannelId();
        String channelOrderId = orderPojo.getChannelOrderId();
        return orderDao.select(clientId, channelId, channelOrderId);
    }

    public OrderPojo getOrderById(Long orderId) {
        return orderDao.select(orderId);
    }

    public OrderData getOrderDataById(Long orderId) {
        return convertOrderPojoToOrderData(getOrderById(orderId));
    }

    private OrderData convertOrderPojoToOrderData(OrderPojo pojo) {
        OrderData data = new OrderData();
        data.setChannelOrderId(pojo.getChannelOrderId());
        data.setStatus(pojo.getStatus());
        data.setChannelId(pojo.getChannelId());
        data.setClientId(pojo.getClientId());
        data.setCustomerId(pojo.getCustomerId());
        return data;
    }

    public OrderPojo getOrderByClientIdChannelIdChannelOrderId(Long clientId, Long channelId, String channelOrderId) {
        return orderDao.select(clientId, channelId, channelOrderId);
    }

    private boolean checkIfOrderWithSameUniqueConstraintsFound(OrderPojo orderPojo) throws ApiException {
        Long clientId = orderPojo.getClientId();
        Long channelId = orderPojo.getChannelId();
        String channelOrderId = orderPojo.getChannelOrderId();
        boolean found = getOrderByClientIdChannelIdChannelOrderId(clientId, channelId, channelOrderId) != null;
        if (orderDao.select(channelOrderId) != null) {
            found = true;
        }
        return found;
    }

    public void setInvoiceUrl(Long orderId, String url) {
        orderDao.update(orderId, url);
    }

    public void updateOrderStatus(Long orderId, OrderStatus status) {
        OrderPojo orderPojo = getOrderById(orderId);
        orderPojo.setStatus(status);
    }
}
