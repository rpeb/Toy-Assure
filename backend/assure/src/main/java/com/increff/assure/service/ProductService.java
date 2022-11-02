package com.increff.assure.service;

import com.increff.assure.dao.ProductDao;
import com.increff.assure.model.ProductDetailsUpdateForm;
import com.increff.assure.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDao dao;

    @Transactional
    public void bulkUpload(List<ProductPojo> productPojos) {
        for (ProductPojo pojo : productPojos) {
            dao.insert(pojo);
        }
    }

    @Transactional(readOnly = true)
    public ProductPojo getById(long globalSkuId) {
        return dao.select(globalSkuId);
    }

    @Transactional(readOnly = true)
    public List<ProductPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional
    public void update(ProductDetailsUpdateForm updateForm) {
        dao.update(updateForm);
    }
}
