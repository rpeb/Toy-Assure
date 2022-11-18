package com.increff.assure.dto;

import com.increff.assure.api.InventoryApi;
import com.increff.assure.pojo.BinSkuPojo;
import com.increff.assure.pojo.InventoryPojo;
import com.increff.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class InventoryDto {

    @Autowired
    private InventoryApi inventoryApi;

    public void updateInventory(List<BinSkuPojo> uploadedBinSkus) {
        List<InventoryPojo> inventoryPojos = new ArrayList<>();
        for (BinSkuPojo binSkuPojo: uploadedBinSkus) {
            InventoryPojo exists = inventoryApi.getByGlobalSkuId(binSkuPojo.getGlobalSkuId());
            if (isNull(exists)) {
                InventoryPojo inventoryPojo = new InventoryPojo();
                inventoryPojo.setGlobalSkuId(binSkuPojo.getGlobalSkuId());
                inventoryPojo.setAvailableQuantity(binSkuPojo.getQuantity());
                inventoryPojo.setAllocatedQuantity(0L);
                inventoryPojo.setFulfilledQuantity(0L);
                inventoryPojos.add(inventoryPojo);
            } else {
                exists.setAvailableQuantity(exists.getAvailableQuantity() + binSkuPojo.getQuantity());
            }
        }
        inventoryApi.upload(inventoryPojos);
    }
}
