package com.increff.assure.model.data;

import com.increff.assure.pojo.OrderStatus;
import lombok.Data;

@Data
public class OrderData {
    private Long clientId;

    private Long customerId;

    private Long channelId;

    private String channelOrderId;

    private OrderStatus status;
}
