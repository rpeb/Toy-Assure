package com.increff.assure.model.form;

import lombok.Data;

@Data
public class InternalOrderItemForm {
    private String clientSkuId;
    private Long orderedQuantity;
    private Double sellingPricePerUnit;
}
