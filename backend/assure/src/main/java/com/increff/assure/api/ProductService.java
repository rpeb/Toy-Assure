package com.increff.assure.api;

import com.increff.assure.dao.ProductDao;
import com.increff.assure.exception.ApiException;
import com.increff.assure.model.ErrorData;
import com.increff.assure.model.ProductDetailsUpdateForm;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.pojo.UserPojo;
import com.increff.assure.pojo.UserType;
import com.increff.assure.util.ValidationUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class ProductService {

    public static final Logger LOGGER = LogManager.getLogger(ProductService.class);

    @Autowired
    private ProductDao dao;

    @Autowired
    private UserService userService;

    @Transactional
    public void bulkUpload(List<ProductPojo> productPojos) throws ApiException {
        Long clientId = productPojos.get(0).getClientId();
        throwsIfClientIdNotFound(clientId);
        List<ProductPojo> productsFoundForClient = dao.selectByClientId(clientId);
        List<ErrorData> errorDataList = new ArrayList<>();
        Set<String> clientSkuIdSet = productsFoundForClient.stream()
                .map(ProductPojo::getClientSkuId)
                .collect(Collectors.toSet());

        AtomicReference<Long> row = new AtomicReference<>(1L);

        productPojos
                .stream()
                .map(ProductPojo::getClientSkuId)
                .forEach(clientskuid -> {
                    if (clientSkuIdSet.contains(clientskuid)) {
                        errorDataList.add(new ErrorData(row.get(), "clientsku id: " + clientskuid + " already present"));
                        row.getAndSet(row.get() + 1);
                    }
                });
        ValidationUtil.throwsIfNotEmpty(errorDataList);

        productPojos
                .forEach(productPojo -> dao.insert(productPojo));
    }

    @Transactional(readOnly = true)
    public ProductPojo getById(Long globalSkuId) throws ApiException {
        throwsIfGlobalSkuIdNotFound(globalSkuId);
        return dao.select(globalSkuId);
    }

    @Transactional(readOnly = true)
    public List<ProductPojo> getByClientId(Long clientId) throws ApiException {
        throwsIfClientIdNotFound(clientId);
        return dao.selectByClientId(clientId);
    }

    @Transactional(readOnly = true)
    public ProductPojo getByClientIdAndClientSkuId(Long clientId, String clientSkuId) throws ApiException {
        throwsIfClientIdNotFound(clientId);
        return dao.selectByClientIdAndClientSkuId(clientId, clientSkuId);
    }

    @Transactional(readOnly = true)
    public List<ProductPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional
    public void update(ProductDetailsUpdateForm updateForm) throws ApiException {
        throwsIfClientIdNotFound(updateForm.getClientId());
        dao.update(updateForm);
    }

    @Transactional(readOnly = true)
    private void throwsIfGlobalSkuIdNotFound(Long globalSkuId) throws ApiException {
        ProductPojo p = dao.select(globalSkuId);
        if (p == null) {
            throw new ApiException("invalid global sku id: " + globalSkuId);
        }
    }

    @Transactional(readOnly = true)
    private void throwsIfClientIdNotFound(Long clientId) throws ApiException {
        UserPojo p = userService.getUserById(clientId);
        if (p == null || p.getType() != UserType.CLIENT) {
            throw new ApiException("invalid client with id: " + clientId);
        }
    }
}
