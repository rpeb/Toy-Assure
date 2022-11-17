package com.increff.commons.model.form;

import lombok.Data;

import java.util.List;

@Data
public class ChannelOrderForm {
    private Long clientId;
    private Long customerId;
    private Long channelId;
    private String channelOrderId;
    private List<ChannelOrderItemForm> channelOrderItems;
}
