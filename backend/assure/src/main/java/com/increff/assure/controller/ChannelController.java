package com.increff.assure.controller;

import com.increff.assure.dto.ChannelDto;
import com.increff.assure.exception.ApiException;
import com.increff.assure.model.ChannelForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private ChannelDto dto;

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "create new channel")
    public void add(@RequestBody ChannelForm channelForm) throws ApiException {
        dto.add(channelForm);
    }
}
