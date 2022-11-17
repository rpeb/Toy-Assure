package com.increff.assure.api;

import com.increff.assure.dao.ProductDao;
import com.increff.assure.model.data.ErrorData;
import com.increff.assure.model.form.ProductDetailsUpdateForm;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.util.ValidationUtil;
import com.increff.commons.exception.ApiException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ProductApi {

    public static final Logger LOGGER = LogManager.getLogger(ProductApi.class);

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserApi userApi;

    @Transactional
    public void bulkUpload(List<ProductPojo> productPojos) throws ApiException {
        Long clientId = productPojos.get(0).getClientId();
        userApi.throwsIfInvalidClientId(clientId);
        List<ErrorData> errorDataList = new ArrayList<>();
        AtomicReference<Long> row = new AtomicReference<>(1L);

        productPojos
                .stream()
                .map(ProductPojo::getClientSkuId)
                .forEach(clientSkuId -> {
                    if (productDao.selectByClientIdAndClientSkuId(clientId, clientSkuId) != null) {
                        errorDataList.add(
                                new ErrorData(
                                        row.get(),
                                        "clientskuid: " + clientSkuId + " already present")
                        );
                    }
                    row.getAndSet(row.get() + 1);
                });
        ValidationUtil.throwsIfNotEmpty(errorDataList);

        productPojos
                .forEach(productPojo -> productDao.insert(productPojo));
    }

    @Transactional(readOnly = true)
    public ProductPojo getById(Long globalSkuId) throws ApiException {
        return productDao.select(globalSkuId);
    }

    @Transactional(readOnly = true)
    public List<ProductPojo> getByClientId(Long clientId) throws ApiException {
        userApi.throwsIfInvalidClientId(clientId);
        return productDao.selectByClientId(clientId);
    }

    @Transactional(readOnly = true)
    public ProductPojo getByClientIdAndClientSkuId(Long clientId, String clientSkuId) {
        return productDao.selectByClientIdAndClientSkuId(clientId, clientSkuId);
    }

    @Transactional(readOnly = true)
    public List<ProductPojo> getAll() {
        return productDao.selectAll();
    }

    @Transactional
    public void update(ProductDetailsUpdateForm updateForm) throws ApiException {
        userApi.throwsIfInvalidClientId(updateForm.getClientId());
        productDao.update(updateForm);
    }

    @Transactional(readOnly = true)
    public void throwsIfGlobalSkuIdNotFound(Long globalSkuId) throws ApiException {
        ProductPojo p = productDao.select(globalSkuId);
        if (p == null) {
            throw new ApiException("invalid globalskuid: " + globalSkuId);
        }
    }
}
