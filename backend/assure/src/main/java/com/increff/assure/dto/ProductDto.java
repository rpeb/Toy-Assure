package com.increff.assure.dto;

import com.increff.assure.dto.helper.ProductDtoHelper;
import com.increff.assure.exception.ApiException;
import com.increff.assure.model.ProductData;
import com.increff.assure.model.ProductDetailsUpdateForm;
import com.increff.assure.model.ProductForm;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductDto {

    @Autowired
    private ProductService service;

    @Transactional
    public void bulkUpload(List<ProductForm> productFormList, long clientId) throws ApiException {
        ProductDtoHelper.normalizeList(productFormList);
        List<ProductPojo> productPojos = ProductDtoHelper.convertListOfProductFormToListOfProductPojo(productFormList, clientId);
        service.bulkUpload(productPojos);
    }

    @Transactional
    public void update(ProductDetailsUpdateForm updateForm) throws ApiException {
        ProductDtoHelper.normalizeUpdateForm(updateForm);
        service.update(updateForm);
    }

    @Transactional(readOnly = true)
    public List<ProductData> getAll() {
        return ProductDtoHelper.convertListOfProductPojoToListOfProductData(service.getAll());
    }

    @Transactional(readOnly = true)
    public ProductData getById(long globalSkuId) {
        return ProductDtoHelper.convertProductPojoToProductData(service.getById(globalSkuId));
    }
}
