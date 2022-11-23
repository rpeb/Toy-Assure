package com.increff.assure.model.form;

import lombok.Data;

@Data
public class BinSkuUpdateForm {
    private Long binId;
    private Long clientId;
    private String clientSkuId;
    private Long quantity;
}
