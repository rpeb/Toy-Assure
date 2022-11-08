package com.increff.assure.controller;

import com.increff.assure.dto.BinSkuDto;
import com.increff.assure.exception.ApiException;
import com.increff.assure.model.BinSkuForm;
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
}
