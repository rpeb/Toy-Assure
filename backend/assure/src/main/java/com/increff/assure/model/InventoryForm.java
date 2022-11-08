package com.increff.assure.model;

import lombok.Data;

@Data
public class InventoryForm {
    private Long clientId;
    private String clientSkuId;
    private Long quantity;
}
