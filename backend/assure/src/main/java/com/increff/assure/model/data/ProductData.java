package com.increff.assure.model.data;

import lombok.Data;

@Data
public class ProductData {
    private String clientSkuId;
    private Long clientId;
    private String name;
    private String brandId;
    private Double mrp;
    private String description;
}
