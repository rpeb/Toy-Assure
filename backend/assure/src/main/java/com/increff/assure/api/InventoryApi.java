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
@Transactional(rollbackFor = ApiException.class)
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

    @Transactional(readOnly = true)
    public InventoryPojo getByGlobalSkuId(Long globalSkuId) {
        return inventoryDao.selectByGlobalSkuId(globalSkuId);
    }

    public void updateAvailableQuantity(Long globalSkuId, Long quantity) throws ApiException {
        InventoryPojo inventoryPojo = getByGlobalSkuId(globalSkuId);
        inventoryPojo.setAvailableQuantity(quantity);
    }


    public void upload(List<InventoryPojo> inventoryPojoList) {
        for (InventoryPojo inventoryPojo : inventoryPojoList) {
            inventoryDao.insert(inventoryPojo);
        }
    }

    public void updateAllocatedQuantity(Long globalSkuId, Long quantity) {
        InventoryPojo inventoryPojo = getByGlobalSkuId(globalSkuId);
        inventoryPojo.setAllocatedQuantity(quantity);
    }
}
