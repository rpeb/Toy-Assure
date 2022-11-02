package com.increff.assure.model;

import lombok.Data;

@Data
public class ProductDetailsUpdateForm {
    private Long clientId;
    private String clientSkuId;
    private String name;
    private String brandId;
    private Double mrp;
    private String description;
}
