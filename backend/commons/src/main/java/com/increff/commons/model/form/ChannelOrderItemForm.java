package com.increff.commons.model.form;

import lombok.Data;

@Data
public class ChannelOrderItemForm {
    private String channelSkuId;
    private Long orderedQuantity;
    private Double sellingPricePerUnit;
}
