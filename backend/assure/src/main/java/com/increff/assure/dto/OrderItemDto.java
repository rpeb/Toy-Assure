package com.increff.assure.dto;

import com.increff.assure.api.ChannelListingApi;
import com.increff.assure.api.OrderItemApi;
import com.increff.assure.api.ProductApi;
import com.increff.assure.model.form.InternalOrderItemForm;
import com.increff.assure.pojo.OrderItemPojo;
import com.increff.assure.pojo.OrderPojo;
import com.increff.commons.exception.ApiException;
import com.increff.commons.model.form.ChannelOrderItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderItemDto {

    @Autowired
    private OrderItemApi orderItemApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private ChannelListingApi channelListingApi;


    public void createInternalOrderItems(List<InternalOrderItemForm> internalOrderItems, OrderPojo insertedOrderPojo) throws ApiException {
        orderItemApi.createOrderItems(convertInternalOrderItemsToOrderItemPojos(internalOrderItems, insertedOrderPojo));
    }

    private List<OrderItemPojo> convertInternalOrderItemsToOrderItemPojos(List<InternalOrderItemForm> internalOrderItems, OrderPojo insertedOrderPojo) {
        List<OrderItemPojo> orderItemPojos = new ArrayList<>();
        for (InternalOrderItemForm itemForm : internalOrderItems) {
            orderItemPojos.add(convertInternalOrderItemFormToOrderItemPojo(itemForm, insertedOrderPojo));
        }
        return orderItemPojos;
    }

    private OrderItemPojo convertInternalOrderItemFormToOrderItemPojo(InternalOrderItemForm itemForm, OrderPojo insertedOrderPojo) {
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderedQuantity(itemForm.getOrderedQuantity());
        orderItemPojo.setAllocatedQuantity(0L);
        orderItemPojo.setOrderId(insertedOrderPojo.getId());
        orderItemPojo.setFulfilledQuantity(0L);
        orderItemPojo.setSellingPricePerUnit(itemForm.getSellingPricePerUnit());
        Long globalSkuId = productApi.getByClientIdAndClientSkuId(
                insertedOrderPojo.getClientId(), itemForm.getClientSkuId()
        ).getGlobalSkuId();
        orderItemPojo.setGlobalSkuId(globalSkuId);
        return orderItemPojo;
    }

    public void createChannelOrderItems(List<ChannelOrderItemForm> channelOrderItems, OrderPojo insertedOrderPojo) throws ApiException {
        orderItemApi.createOrderItems(convertChannelOrderItemsToOrderItemPojos(channelOrderItems, insertedOrderPojo));
    }

    private List<OrderItemPojo> convertChannelOrderItemsToOrderItemPojos(List<ChannelOrderItemForm> channelOrderItems, OrderPojo insertedOrderPojo) {
        List<OrderItemPojo> orderItemPojos = new ArrayList<>();
        for (ChannelOrderItemForm itemForm : channelOrderItems) {
            orderItemPojos.add(convertChannelOrderItemFormToOrderItemPojo(itemForm, insertedOrderPojo));
        }
        return orderItemPojos;
    }

    private OrderItemPojo convertChannelOrderItemFormToOrderItemPojo(ChannelOrderItemForm itemForm, OrderPojo insertedOrderPojo) {
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderedQuantity(itemForm.getOrderedQuantity());
        orderItemPojo.setAllocatedQuantity(0L);
        orderItemPojo.setOrderId(insertedOrderPojo.getId());
        orderItemPojo.setFulfilledQuantity(0L);
        orderItemPojo.setSellingPricePerUnit(itemForm.getSellingPricePerUnit());
        Long globalSkuId = channelListingApi.getByClientIdChannelIdChannelSkuId(
                insertedOrderPojo.getClientId(),
                insertedOrderPojo.getChannelId(),
                itemForm.getChannelSkuId()
        ).getGlobalSkuId();
        orderItemPojo.setGlobalSkuId(globalSkuId);
        return orderItemPojo;
    }
}
