package com.increff.assure.model.data;

import lombok.Data;

@Data
public class ProductData {
    private Long globalSkuId;
    private String clientSkuId;
    private Long clientId;
    private String name;
    private String brandId;
    private Double mrp;
    private String description;
}
