package com.increff.assure.api;

import com.increff.assure.dao.InventoryDao;
import com.increff.assure.pojo.InventoryPojo;
import com.increff.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class InventoryApi {

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private ProductApi productApi;

    @Transactional(readOnly = true)
    public List<InventoryPojo> getAll() {
        return inventoryDao.select();
    }

    @Transactional(readOnly = true)
    public InventoryPojo getById(Long id) throws ApiException {
        throwsIfIdNotFound(id);
        return inventoryDao.selectById(id);
    }

    private void throwsIfIdNotFound(Long id) throws ApiException {
        InventoryPojo inventoryPojo = inventoryDao.selectById(id);
        if (inventoryPojo == null) {
            throw new ApiException("invalid inventory id: " + id);
        }
    }

    //
    @Transactional(readOnly = true)
    public InventoryPojo getByGlobalSkuId(Long globalSkuId) throws ApiException {
        productApi.throwsIfGlobalSkuIdNotFound(globalSkuId);
        return inventoryDao.selectByGlobalSkuId(globalSkuId);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void updateAvailableQuantity(Long globalSkuId, Long quantity) throws ApiException {
        InventoryPojo inventoryPojo = getByGlobalSkuId(globalSkuId);
        inventoryPojo.setAvailableQuantity(inventoryPojo.getAvailableQuantity() + quantity);
    }


    @Transactional(rollbackFor = ApiException.class)
    public void upload(List<InventoryPojo> inventoryPojoList) {
        for (InventoryPojo inventoryPojo : inventoryPojoList) {
            InventoryPojo exists = inventoryDao.selectByGlobalSkuId(inventoryPojo.getGlobalSkuId());
            if (isNull(exists)) {
                inventoryPojo.setFulfilledQuantity(0L);
                inventoryPojo.setAllocatedQuantity(0L);
                inventoryDao.insert(inventoryPojo);
            } else {
                exists.setAvailableQuantity(exists.getAvailableQuantity() + inventoryPojo.getAvailableQuantity());
            }
        }
    }
}
