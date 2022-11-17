package com.increff.assure.dto.helper;

import com.increff.assure.pojo.InventoryPojo;

public class InventoryHelper {

    public static InventoryPojo initialize(Long globalSkuId, Long quantity) {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setGlobalSkuId(globalSkuId);
        inventoryPojo.setAvailableQuantity(quantity);
        return inventoryPojo;
    }
}
