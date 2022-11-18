package com.increff.assure.dto;

import com.increff.assure.api.*;
import com.increff.assure.dto.helper.OrderDtoHelper;
import com.increff.assure.model.data.OrderItemData;
import com.increff.assure.model.form.InternalOrderForm;
import com.increff.assure.model.form.InternalOrderItemForm;
import com.increff.assure.pojo.BinPojo;
import com.increff.assure.pojo.OrderItemPojo;
import com.increff.assure.pojo.OrderPojo;
import com.increff.assure.pojo.OrderStatus;
import com.increff.commons.exception.ApiException;
import com.increff.commons.model.form.ChannelOrderForm;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderDto {

    public static final Logger LOGGER = LogManager.getLogger(OrderDto.class);

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private OrderItemApi orderItemApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private BinSkuDto binSkuDto;

    @Autowired
    private OrderItemDto orderItemDto;

    @Autowired
    private OrderDtoHelper orderDtoHelper;

    public void createInternalOrder(InternalOrderForm orderForm) throws ApiException {
        OrderDtoHelper.validateInternalOrder(orderForm);
        OrderDtoHelper.validateInternalOrderItems(orderForm.getInternalOrderItems());
        OrderDtoHelper.throwsIfDuplicateClientSkus(orderForm.getInternalOrderItems());
        userApi.throwsIfInvalidClientId(orderForm.getClientId());
        userApi.throwsIfInvalidCustomerId(orderForm.getCustomerId());
        List<String> clientSkuIdsList = orderForm.getInternalOrderItems()
                .stream()
                .map(InternalOrderItemForm::getClientSkuId)
                .collect(Collectors.toList());
        orderDtoHelper.throwsIfInvalidClientSkuId(orderForm.getClientId(), clientSkuIdsList);
        OrderPojo insertedOrderPojo = orderApi.createOrder(orderDtoHelper.convertInternalOrderFormToOrderPojo(orderForm));
        orderItemDto.createInternalOrderItems(orderForm.getInternalOrderItems(), insertedOrderPojo);
    }

    public void createChannelOrder(ChannelOrderForm orderForm) throws ApiException {
        orderDtoHelper.throwsIfInternalChannelIdForChannelOrder(orderForm.getChannelId());
        OrderDtoHelper.validateChannelOrder(orderForm);
        OrderDtoHelper.validateChannelOrderItems(orderForm.getChannelOrderItems());
        OrderDtoHelper.throwsIfDuplicateChannelSkus(orderForm.getChannelOrderItems());
        userApi.throwsIfInvalidClientId(orderForm.getClientId());
        userApi.throwsIfInvalidCustomerId(orderForm.getCustomerId());
        LOGGER.info("orderForm = " + orderForm);
        OrderPojo insertedOrderPojo = orderApi.createOrder(OrderDtoHelper.convertChannelOrderFormToOrderPojo(orderForm));
        LOGGER.info("insertedOrderPojo = " + insertedOrderPojo);
        orderItemDto.createChannelOrderItems(orderForm.getChannelOrderItems(), insertedOrderPojo);
    }

    public void allocateOrder(Long orderId) throws ApiException {
        orderDtoHelper.throwsIfOrderStatusNotCreated(orderId);
        List<OrderItemPojo> orderItems = orderItemApi.getOrderItemsByOrderId(orderId);
        int numberOfOrderedItems = orderItems.size();
        int numberOfItemsFullyAllocated = 0;
        Map<Long, Long> inventoryQuantityMap = orderDtoHelper.getMapOfInventoryQuantities(orderItems);
        for (OrderItemPojo orderItem: orderItems) {
            Long quantityInInventory = inventoryQuantityMap.get(orderItem.getGlobalSkuId());
            Long allocatedQuantity = orderItem.getAllocatedQuantity();
            Long orderedQuantity = orderItem.getOrderedQuantity();
            Long quantitiesRequired = orderedQuantity - allocatedQuantity;
            if (quantitiesRequired <= quantityInInventory) {
                // we have sufficient items in the inventory
                orderItem.setAllocatedQuantity(orderedQuantity);
                inventoryApi.updateAllocatedQuantity(orderItem.getGlobalSkuId(), orderedQuantity);
                inventoryApi.updateAvailableQuantity(orderItem.getGlobalSkuId(), quantityInInventory - orderedQuantity);
                binSkuDto.updateAvailableQuantity(orderItem.getGlobalSkuId(), quantityInInventory - orderedQuantity);
            } else {
                orderItem.setAllocatedQuantity(orderItem.getAllocatedQuantity() + quantityInInventory);
                inventoryApi.updateAvailableQuantity(orderItem.getGlobalSkuId(), 0L);
                inventoryApi.updateAllocatedQuantity(orderItem.getGlobalSkuId(), orderItem.getAllocatedQuantity());
            }
            if (orderItem.getOrderedQuantity().equals(orderItem.getAllocatedQuantity())) {
                numberOfItemsFullyAllocated++;
            }
        }
        if (numberOfItemsFullyAllocated == numberOfOrderedItems) {
            orderApi.updateOrderStatus(orderId, OrderStatus.ALLOCATED);
        }
    }

    public String getInvoice(Long orderId) throws ApiException, IOException, TransformerException {
        return orderDtoHelper.getInvoice(orderId);
    }

}
