package com.increff.assure.api;

import com.increff.assure.dao.InventoryDao;
import com.increff.assure.dao.ProductDao;
import com.increff.assure.exception.ApiException;
import com.increff.assure.pojo.InventoryPojo;
import com.increff.assure.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryDao dao;

    // TODO: call api methods instead of dao methods

    @Autowired
    private ProductDao productDao;

    @Transactional(readOnly = true)
    public List<InventoryPojo> getAll() {
        return dao.select();
    }

    @Transactional(readOnly = true)
    public InventoryPojo getById(Long id) throws ApiException {
        throwsIfIdNotFound(id);
        return dao.selectById(id);
    }

    private void throwsIfIdNotFound(Long id) throws ApiException {
        InventoryPojo inventoryPojo = dao.selectById(id);
        if (inventoryPojo == null) {
            throw new ApiException("invalid inventory id: " + id);
        }
    }

    @Transactional(readOnly = true)
    public InventoryPojo getByGlobalSkuId(Long globalSkuId) throws ApiException {
        throwsIfglobalSkuIdNotFound(globalSkuId);
        return dao.selectByGlobalSkuId(globalSkuId);
    }

    private void throwsIfglobalSkuIdNotFound(Long globalSkuId) throws ApiException {
        ProductPojo productPojo = productDao.select(globalSkuId);
        if (productPojo == null) {
            throw new ApiException("invalid global sku id: " + globalSkuId);
        }
    }

    @Transactional
    public void updateAvailableQuantity(Long globalSkuId, Long quantity) throws ApiException {
        InventoryPojo inventoryPojo = getByGlobalSkuId(globalSkuId);
        inventoryPojo.setAvailableQuantity(inventoryPojo.getAvailableQuantity() + quantity);
    }

    @Transactional
    public void add(InventoryPojo p) throws ApiException {
        InventoryPojo inventoryPojo = dao.selectByGlobalSkuId(p.getGlobalSkuId());
        if (inventoryPojo == null) {
            p.setAllocatedQuantity(0L);
            p.setFulfilledQuantity(0L);
            dao.insert(p);
        } else {
            Long availableQuantity = p.getAvailableQuantity();
            dao.update(inventoryPojo, availableQuantity);
        }
    }
}
