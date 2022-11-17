package com.increff.assure.dto.helper;


import com.increff.assure.model.form.InternalOrderForm;
import com.increff.assure.model.form.InternalOrderItemForm;
import com.increff.assure.pojo.OrderPojo;
import com.increff.assure.pojo.OrderStatus;
import com.increff.commons.exception.ApiException;
import com.increff.commons.model.form.ChannelOrderForm;
import com.increff.commons.model.form.ChannelOrderItemForm;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;

public class OrderDtoHelper {

    public static OrderPojo convertInternalOrderFormToOrderPojo(InternalOrderForm orderForm) {
        OrderPojo pojo = new OrderPojo();
        pojo.setChannelId(null);
        pojo.setChannelOrderId(orderForm.getChannelOrderId());
        pojo.setStatus(OrderStatus.CREATED);
        pojo.setClientId(orderForm.getClientId());
        pojo.setCustomerId(orderForm.getCustomerId());
        return pojo;
    }

    public static void throwsIfDuplicateClientSkus(List<InternalOrderItemForm> internalOrderItems) throws ApiException {
        Set<String> clientSkuIds = new HashSet<>();
        Long row = 1L;
        for (InternalOrderItemForm itemForm: internalOrderItems) {
            if (clientSkuIds.contains(itemForm.getClientSkuId())) {
                throw new ApiException("row: " + row + " duplicate clientskuid: " + itemForm.getClientSkuId() + " found");
            } else {
                clientSkuIds.add(itemForm.getClientSkuId());
            }
            row++;
        }
    }

    public static void validateInternalOrderItems(List<InternalOrderItemForm> internalOrderItems) throws ApiException {
        if (CollectionUtils.isEmpty(internalOrderItems)) {
            throw new ApiException("order items list is empty");
        }
        Long row = 1L;
        for (InternalOrderItemForm itemForm : internalOrderItems) {
            if (isNull(itemForm.getClientSkuId()) || itemForm.getClientSkuId().isEmpty()) {
                throw new ApiException("row: " + row + " clientSkuid is empty");
            }
            if (isNull(itemForm.getOrderedQuantity())) {
                throw new ApiException("row: " + row + " ordered quantity is empty");
            }
            if (itemForm.getOrderedQuantity() < 0) {
                throw new ApiException("row: " + row + " ordered quantity is negative");
            }
            if (isNull(itemForm.getSellingPricePerUnit())) {
                throw new ApiException("row: " + row + " selling price per unit is negative");
            }
            if (itemForm.getSellingPricePerUnit() < 0) {
                throw new ApiException("row: " + row + " selling price per unit is negative");
            }
            row++;
        }
    }

    public static void validateInternalOrder(InternalOrderForm orderForm) throws ApiException {
        if (isNull(orderForm)) {
            throw new ApiException("order form is empty");
        }
        if (isNull(orderForm.getChannelOrderId()) || orderForm.getChannelOrderId().isEmpty()) {
            throw new ApiException("channel order id not found");
        }
        if (isNull(orderForm.getClientId())) {
            throw new ApiException("client id not found");
        }
        if (isNull(orderForm.getCustomerId())) {
            throw new ApiException("customer id not found");
        }
        if (isNull(orderForm.getInternalOrderItems())) {
            throw new ApiException("order csv not found");
        }
    }

    public static void validateChannelOrder(ChannelOrderForm orderForm) throws ApiException {
        if (isNull(orderForm)) {
            throw new ApiException("order form is empty");
        }
        if (isNull(orderForm.getChannelOrderId()) || orderForm.getChannelOrderId().isEmpty()) {
            throw new ApiException("channel order id not found");
        }
        if (isNull(orderForm.getClientId())) {
            throw new ApiException("client id not found");
        }
        if (isNull(orderForm.getCustomerId())) {
            throw new ApiException("customer id not found");
        }
        if (isNull(orderForm.getChannelId())) {
            throw new ApiException("channel id not found");
        }
        if (isNull(orderForm.getChannelOrderItems())) {
            throw new ApiException("order csv not found");
        }
    }

    public static void validateChannelOrderItems(List<ChannelOrderItemForm> channelOrderItems) throws ApiException {
        if (CollectionUtils.isEmpty(channelOrderItems)) {
            throw new ApiException("order items list is empty");
        }
        Long row = 1L;
        for (ChannelOrderItemForm itemForm : channelOrderItems) {
            if (isNull(itemForm.getChannelSkuId()) || itemForm.getChannelSkuId().isEmpty()) {
                throw new ApiException("row: " + row + " channel sku id is empty");
            }
            if (isNull(itemForm.getOrderedQuantity())) {
                throw new ApiException("row: " + row + " ordered quantity is empty");
            }
            if (itemForm.getOrderedQuantity() < 0) {
                throw new ApiException("row: " + row + " ordered quantity is negative");
            }
            if (isNull(itemForm.getSellingPricePerUnit())) {
                throw new ApiException("row: " + row + " selling price per unit is negative");
            }
            if (itemForm.getSellingPricePerUnit() < 0) {
                throw new ApiException("row: " + row + " selling price per unit is negative");
            }
            row++;
        }
    }

    public static void throwsIfDuplicateChannelSkus(List<ChannelOrderItemForm> channelOrderItems) throws ApiException {
        Set<String> clientSkuIds = new HashSet<>();
        Long row = 1L;
        for (ChannelOrderItemForm itemForm: channelOrderItems) {
            if (clientSkuIds.contains(itemForm.getChannelSkuId())) {
                throw new ApiException("row: " + row + " duplicate clientskuid: " + itemForm.getChannelSkuId() + " found");
            } else {
                clientSkuIds.add(itemForm.getChannelSkuId());
            }
            row++;
        }
    }

    public static OrderPojo convertChannelOrderFormToOrderPojo(ChannelOrderForm orderForm) {
        OrderPojo pojo = new OrderPojo();
        pojo.setCustomerId(orderForm.getCustomerId());
        pojo.setChannelId(orderForm.getChannelId());
        pojo.setStatus(OrderStatus.CREATED);
        pojo.setChannelOrderId(orderForm.getChannelOrderId());
        pojo.setClientId(orderForm.getClientId());
        return pojo;
    }
}
