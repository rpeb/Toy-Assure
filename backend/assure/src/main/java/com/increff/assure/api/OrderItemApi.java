package com.increff.assure.api;

import com.increff.assure.dao.OrderItemDao;
import com.increff.assure.model.data.OrderItemData;
import com.increff.assure.pojo.OrderItemPojo;
import com.increff.assure.pojo.ProductPojo;
import com.increff.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderItemApi {


    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ProductApi productApi;

    public void createOrderItems(List<OrderItemPojo> orderItemPojos) throws ApiException {
        for (OrderItemPojo orderItemPojo : orderItemPojos) {
            boolean found = checkIfOrderItemsWithSameUniqueConstraintsFound(orderItemPojo);
            if (!found) {
                orderItemDao.insert(orderItemPojo);
            }
        }
    }

    public List<OrderItemPojo> getOrderItemsByOrderId(Long orderId) throws ApiException {
        return orderItemDao.select(orderId);
    }

    public List<OrderItemData> getOrderItemDataListByOrderId(Long orderId) throws ApiException {
        List<OrderItemPojo> orderItemPojos = orderItemDao.select(orderId);
        return convertOrderItemPojoListToOrderItemDataList(orderItemPojos);
    }

    private List<OrderItemData> convertOrderItemPojoListToOrderItemDataList(List<OrderItemPojo> orderItemPojos) throws ApiException {
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for (OrderItemPojo orderItemPojo : orderItemPojos) {
            orderItemDataList.add(convertOrderItemPojoToOrderItemData(orderItemPojo));
        }
        return orderItemDataList;
    }

    private OrderItemData convertOrderItemPojoToOrderItemData(OrderItemPojo orderItemPojo) throws ApiException {
        OrderItemData data = new OrderItemData();
        data.setOrderId(orderItemPojo.getOrderId());
        data.setOrderedQuantity(orderItemPojo.getOrderedQuantity());
        ProductPojo productPojo = productApi.getById(orderItemPojo.getGlobalSkuId());
        data.setClientSkuId(productPojo.getClientSkuId());
        data.setProductName(productPojo.getName());
        data.setSellingPricePerUnit(orderItemPojo.getSellingPricePerUnit());
        return data;
    }


    private boolean checkIfOrderItemsWithSameUniqueConstraintsFound(OrderItemPojo orderItemPojo) throws ApiException {
        return orderItemDao.select(orderItemPojo.getOrderId(), orderItemPojo.getGlobalSkuId()) != null;
    }

    public void updatedAllocatedQuantity(OrderItemPojo orderItemPojo, Long orderedQuantity) {
        orderItemPojo.setAllocatedQuantity(orderItemPojo.getAllocatedQuantity() + orderedQuantity);
    }
}
