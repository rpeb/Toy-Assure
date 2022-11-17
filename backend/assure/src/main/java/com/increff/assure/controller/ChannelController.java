package com.increff.assure.controller;

import com.increff.assure.dto.ChannelDto;
import com.increff.assure.model.form.ChannelForm;
import com.increff.commons.exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
