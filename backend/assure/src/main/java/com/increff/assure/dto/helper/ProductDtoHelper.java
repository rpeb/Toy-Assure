package com.increff.assure.dto.helper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.increff.assure.exception.ApiException;
import com.increff.assure.model.ProductData;
import com.increff.assure.model.ProductDetailsUpdateForm;
import com.increff.assure.model.ProductForm;
import com.increff.assure.pojo.ProductPojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDtoHelper {


    public static ProductPojo convertProductFormToProductPojo(ProductForm productForm, long clientId) {
        return new ProductPojo();
    }

    public static List<ProductPojo> convertListOfProductFormToListOfProductPojo(List<ProductForm> productFormList, long clientId) {
        List<ProductPojo> productPojos = new ArrayList<>();
        for (ProductForm p : productFormList) {
            productPojos.add(convertProductFormToProductPojo(p, clientId));
        }
        return productPojos;
    }

    public static ProductData convertProductPojoToProductData(ProductPojo p) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ProductData d = null;
        try {
            d = objectMapper.readValue(objectMapper.writeValueAsString(p), ProductData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return d;
    }

    public static List<ProductData> convertListOfProductPojoToListOfProductData(List<ProductPojo> productPojos) {
        return productPojos.stream().map((ProductDtoHelper::convertProductPojoToProductData)).collect(Collectors.toList());
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
        if (productForm.getBrandId() == null || productForm.getBrandId().isEmpty()) {
            throw new ApiException("brand id of a product cannot be empty");
        }
        if (productForm.getMrp() == null) {
            throw new ApiException("mrp of product is required");
        }
        if (productForm.getMrp() < 0) {
            throw new ApiException("mrp of product cannot be negative");
        }
        if (productForm.getDescription() == null || productForm.getDescription().isEmpty()) {
            throw new ApiException("description of a product cannot be empty");
        }
    }

    public static void normalize(ProductForm productForm) throws ApiException {
        validate(productForm);
        productForm.setBrandId(productForm.getBrandId().trim().toLowerCase());
        productForm.setClientSkuId(productForm.getClientSkuId().trim().toLowerCase());
        productForm.setName(productForm.getName().trim().toLowerCase());
        productForm.setDescription(productForm.getDescription().trim().toLowerCase());
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
}
