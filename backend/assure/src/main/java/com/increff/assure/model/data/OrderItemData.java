package com.increff.assure.model.data;

import lombok.Data;

@Data
public class OrderItemData {
    private Long orderId;
    private String clientSkuId;
    private String productName;
    private Long orderedQuantity;
    private Double sellingPricePerUnit;
}
