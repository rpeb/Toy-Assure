package com.increff.assure.dto;

import com.increff.assure.api.InventoryApi;
import com.increff.assure.api.OrderApi;
import com.increff.assure.api.OrderItemApi;
import com.increff.assure.api.UserApi;
import com.increff.assure.dto.helper.OrderDtoHelper;
import com.increff.assure.model.form.InternalOrderForm;
import com.increff.assure.model.form.InternalOrderItemForm;
import com.increff.assure.pojo.InventoryPojo;
import com.increff.assure.pojo.OrderItemPojo;
import com.increff.assure.pojo.OrderPojo;
import com.increff.assure.pojo.OrderStatus;
import com.increff.assure.util.InvoiceUploadException;
import com.increff.commons.exception.ApiException;
import com.increff.commons.model.form.ChannelOrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderDto {

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
        OrderPojo insertedOrderPojo = orderApi.createOrder(OrderDtoHelper.convertChannelOrderFormToOrderPojo(orderForm));
        orderItemDto.createChannelOrderItems(orderForm.getChannelOrderItems(), insertedOrderPojo);
    }

    public void allocateOrder(Long orderId) throws ApiException {
        orderDtoHelper.throwsIfOrderStatusNotCreated(orderId);
        List<OrderItemPojo> orderItems = orderItemApi.getOrderItemsByOrderId(orderId);
        int countOfFullyAllocatedItems = 0;
        Map<Long, Long> inventoryQuantityMap = orderDtoHelper.getMapOfInventoryQuantities(orderItems);
        for (OrderItemPojo orderItem : orderItems) {
            Long quantityInInventory = inventoryQuantityMap.get(orderItem.getGlobalSkuId());
            Long allocatedQuantity = orderItem.getAllocatedQuantity();
            Long orderedQuantity = orderItem.getOrderedQuantity();
            Long quantitiesRequired = orderedQuantity - allocatedQuantity;
            Long allocatableQuantity = Math.min(quantityInInventory, quantitiesRequired);
            orderItem.setAllocatedQuantity(allocatableQuantity + allocatedQuantity);
            inventoryApi.updateAllocatedQuantity(orderItem.getGlobalSkuId(), allocatableQuantity);
            inventoryApi.updateAvailableQuantity(orderItem.getGlobalSkuId(), quantityInInventory - allocatableQuantity);
            binSkuDto.updateAvailableQuantity(orderItem.getGlobalSkuId(), allocatableQuantity);
            if (orderItem.getOrderedQuantity().equals(orderItem.getAllocatedQuantity())) {
                countOfFullyAllocatedItems++;
            }
        }
        if (countOfFullyAllocatedItems == orderItems.size()) {
            orderApi.updateOrderStatus(orderId, OrderStatus.ALLOCATED);
        }
    }

    public String getInvoice(Long orderId) throws ApiException, IOException, TransformerException, InvoiceUploadException {
        return orderDtoHelper.getInvoice(orderId);
    }

    public void fulfillOrder(Long orderId) throws ApiException {
        orderDtoHelper.throwsIfOrderStatusNotAllocated(orderId);
        List<OrderItemPojo> orderItems = orderItemApi.getOrderItemsByOrderId(orderId);
        for (OrderItemPojo orderItemPojo : orderItems) {
            InventoryPojo inventoryPojo = inventoryApi.getByGlobalSkuId(orderItemPojo.getGlobalSkuId());
            orderItemPojo.setFulfilledQuantity(orderItemPojo.getOrderedQuantity());
            orderItemPojo.setAllocatedQuantity(0L);
            inventoryPojo.setAllocatedQuantity(inventoryPojo.getAllocatedQuantity() - orderItemPojo.getOrderedQuantity());
            inventoryPojo.setFulfilledQuantity(inventoryPojo.getFulfilledQuantity() + orderItemPojo.getOrderedQuantity());
        }
        orderApi.updateOrderStatus(orderId, OrderStatus.FULFILLED);
    }
}
