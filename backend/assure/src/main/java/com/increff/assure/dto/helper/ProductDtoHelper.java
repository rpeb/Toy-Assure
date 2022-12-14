package com.increff.assure.dto.helper;

import com.increff.assure.model.data.ProductData;
import com.increff.assure.model.form.FetchProductForm;
import com.increff.assure.model.form.ProductDetailsUpdateForm;
import com.increff.assure.model.form.ProductForm;
import com.increff.assure.pojo.ProductPojo;
import com.increff.commons.exception.ApiException;
import com.mysql.cj.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class ProductDtoHelper {


    public static ProductPojo convertProductFormToProductPojo(ProductForm productForm, Long clientId) {
        ProductPojo p = new ProductPojo();
        p.setClientSkuId(productForm.getClientSkuId());
        p.setMrp(productForm.getMrp());
        p.setName(productForm.getName());
        p.setClientId(clientId);
        p.setDescription(productForm.getDescription());
        p.setBrandId(productForm.getBrandId());
        return p;
    }

    public static List<ProductPojo> convertListOfProductFormToListOfProductPojo(List<ProductForm> productFormList, Long clientId) {
        List<ProductPojo> productPojos = new ArrayList<>();
        for (ProductForm p : productFormList) {
            productPojos.add(convertProductFormToProductPojo(p, clientId));
        }
        return productPojos;
    }

    public static ProductData convertProductPojoToProductData(ProductPojo p) {
        if (isNull(p)) {
            return null;
        }
        ProductData d = new ProductData();
        d.setName(p.getName());
        d.setDescription(p.getDescription());
        d.setMrp(p.getMrp());
        d.setBrandId(p.getBrandId());
        d.setClientId(p.getClientId());
        d.setClientSkuId(p.getClientSkuId());
        return d;
    }

    public static List<ProductData> convertListOfProductPojoToListOfProductData(List<ProductPojo> productPojos) {
        List<ProductData> productDataList = new ArrayList<>();
        for (ProductPojo p : productPojos) {
            productDataList.add(convertProductPojoToProductData(p));
        }
        return productDataList;
    }

    public static void validate(ProductForm productForm) throws ApiException {
        if (productForm == null) {
            throw new ApiException("product is missing");
        }
        if (productForm.getClientSkuId() == null || productForm.getClientSkuId().isEmpty()) {
            throw new ApiException("client sku id cannot be empty for product");
        }
        if (productForm.getName() == null || productForm.getName().isEmpty()) {
            throw new ApiException("name of product cannot be empty");
        }
        if (productForm.getMrp() == null) {
            throw new ApiException("mrp of product is required");
        }
        if (productForm.getMrp() < 0) {
            throw new ApiException("mrp of product cannot be negative");
        }
    }

    public static void normalize(ProductForm productForm) throws ApiException {
        validate(productForm);
        if (productForm.getBrandId() != null && !productForm.getBrandId().isEmpty()) {
            productForm.setBrandId(productForm.getBrandId().trim().toLowerCase());
        }
        productForm.setClientSkuId(productForm.getClientSkuId().trim().toLowerCase());
        productForm.setName(productForm.getName().trim().toLowerCase());
        if (productForm.getDescription() != null && !productForm.getDescription().isEmpty()) {
            productForm.setDescription(productForm.getDescription().trim().toLowerCase());
        }
    }

    public static void normalizeList(List<ProductForm> productFormList) throws ApiException {
        for (ProductForm productForm : productFormList) {
            normalize(productForm);
        }
    }

    public static void validateUpdateForm(ProductDetailsUpdateForm updateForm) throws ApiException {
        if (updateForm == null) {
            throw new ApiException("update form cannot be empty");
        }
        if (updateForm.getClientId() == null) {
            throw new ApiException("client id cannot be empty");
        }
        if (updateForm.getClientSkuId() == null || updateForm.getClientSkuId().isEmpty()) {
            throw new ApiException("client sku id cannot be empty");
        }
        if (updateForm.getMrp() < 0) {
            throw new ApiException("mrp cannot be negative");
        }
    }

    public static void normalizeUpdateForm(ProductDetailsUpdateForm updateForm) throws ApiException {
        validateUpdateForm(updateForm);
        updateForm.setClientSkuId(updateForm.getClientSkuId().trim().toLowerCase());
        updateForm.setName(updateForm.getName().trim().toLowerCase());
        updateForm.setBrandId(updateForm.getBrandId().trim().toLowerCase());
        updateForm.setDescription(updateForm.getDescription().trim().toLowerCase());
    }

    public static void validateFetchProductForm(FetchProductForm fetchProductForm) throws ApiException {
        if (isNull(fetchProductForm)) {
            throw new ApiException("form is empty");
        }
        if (isNull(fetchProductForm.getClientId())) {
            throw new ApiException("client id is missing");
        }
        if (StringUtils.isNullOrEmpty(fetchProductForm.getClientSkuId())) {
            throw new ApiException("client sku id is missing");
        }
    }
}
