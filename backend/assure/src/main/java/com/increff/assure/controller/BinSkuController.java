package com.increff.assure.controller;

import com.increff.assure.dto.BinSkuDto;
import com.increff.assure.model.data.BinSkuData;
import com.increff.assure.model.form.BinSkuForm;
import com.increff.assure.model.form.BinSkuUpdateForm;
import com.increff.commons.exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/bin-sku")
public class BinSkuController {

    @Autowired
    private BinSkuDto dto;

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "upload inventory data through csv")
    public void upload(@RequestBody List<BinSkuForm> BinSkuFormList, @RequestParam Long clientId) throws ApiException {
        dto.upload(BinSkuFormList, clientId);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "get all the bin skus")
    public List<BinSkuData> getAll() {
        return dto.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "update the quantity")
    public void updateQuantity(@PathVariable Long id, @RequestBody BinSkuUpdateForm binSkuUpdateForm) throws ApiException {
        dto.updateQuantity(id, binSkuUpdateForm);
    }
}
