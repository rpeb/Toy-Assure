package com.increff.assure.controller;

import com.increff.assure.dto.ProductDto;
import com.increff.assure.model.data.ProductData;
import com.increff.assure.model.form.FetchProductForm;
import com.increff.assure.model.form.ProductDetailsUpdateForm;
import com.increff.assure.model.form.ProductForm;
import com.increff.commons.exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductDto dto;

    /**
     * upload products from a CSV file
     * validations: the products should be uploaded against a client id
     * so ensure the client id is present in the system
     */

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "add list of products")
    public void bulkUpload(@RequestBody List<ProductForm> productFormList, @RequestParam Long clientId) throws ApiException {
        dto.bulkUpload(productFormList, clientId);
    }

    /**
     * edit product details -> name, brandId, mrp, desc
     * do necessary validations
     */
    @RequestMapping(method = RequestMethod.PUT)
    @ApiOperation(value = "update product details")
    public void update(@RequestBody ProductDetailsUpdateForm updateForm) throws ApiException {
        dto.update(updateForm);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "get list of all products")
    public List<ProductData> getAll() {
        return dto.getAll();
    }

    @RequestMapping(value = "/ids", method = RequestMethod.POST)
    @ApiOperation(value = "get a product by clientid and clientSkuId")
    public ProductData getByClientIdAndClientSkuId(@RequestBody FetchProductForm fetchProductForm) throws ApiException {
        return dto.getByClientIdAndClientSkuId(fetchProductForm);
    }

}
