package com.increff.assure.controller;

import com.increff.assure.dto.BinDto;
import com.increff.assure.model.data.BinData;
import com.increff.assure.pojo.BinPojo;
import com.increff.commons.exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
@RequestMapping("/bin")
public class BinController {

    @Autowired
    private BinDto dto;

    // todo: return ids of newly created bins
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "create n bins")
    public List<BinData> addBin(@RequestParam(name = "n") Long numberOfBins) throws ApiException {
        return dto.addBin(numberOfBins);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "get all bin ids")
    public List<BinData> getAll() {
        return dto.getAll();
    }
}
