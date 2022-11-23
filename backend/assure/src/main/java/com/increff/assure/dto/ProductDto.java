package com.increff.assure.dto;

import com.increff.assure.api.ProductApi;
import com.increff.assure.api.UserApi;
import com.increff.assure.dto.helper.ProductDtoHelper;
import com.increff.assure.model.data.ProductData;
import com.increff.assure.model.form.FetchProductForm;
import com.increff.assure.model.form.ProductDetailsUpdateForm;
import com.increff.assure.model.form.ProductForm;
import com.increff.assure.pojo.ProductPojo;
import com.increff.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ProductDto {

    @Autowired
    private ProductApi productApi;

    @Autowired
    private UserApi userApi;

    @Transactional
    public void bulkUpload(List<ProductForm> productFormList, Long clientId) throws ApiException {
        ProductDtoHelper.normalizeList(productFormList);
        List<ProductPojo> productPojos = ProductDtoHelper.convertListOfProductFormToListOfProductPojo(productFormList, clientId);
        productApi.bulkUpload(productPojos);
    }

    @Transactional
    public void update(ProductDetailsUpdateForm updateForm) throws ApiException {
        ProductDtoHelper.normalizeUpdateForm(updateForm);
        productApi.update(updateForm);
    }

    @Transactional(readOnly = true)
    public List<ProductData> getAll() {
        return ProductDtoHelper.convertListOfProductPojoToListOfProductData(productApi.getAll());
    }


    @Transactional(readOnly = true)
    public ProductData getByClientIdAndClientSkuId(FetchProductForm fetchProductForm) throws ApiException {
        ProductDtoHelper.validateFetchProductForm(fetchProductForm);
        userApi.throwsIfInvalidClientId(fetchProductForm.getClientId());
        ProductPojo productPojo = productApi.getByClientIdAndClientSkuId(
                fetchProductForm.getClientId(),
                fetchProductForm.getClientSkuId()
        );
        return ProductDtoHelper.convertProductPojoToProductData(productPojo);
    }
}
