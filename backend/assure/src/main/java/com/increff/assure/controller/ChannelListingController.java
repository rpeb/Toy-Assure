package com.increff.assure.controller;

import com.increff.assure.dto.ChannelListingDto;
import com.increff.assure.model.form.ChannelListingUploadForm;
import com.increff.commons.exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping("/channel-listing")
public class ChannelListingController {

    @Autowired
    private ChannelListingDto dto;

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "upload channel listing for a client and a channel")
    public void add(@RequestBody ChannelListingUploadForm channelListingUploadForm) throws ApiException {
        dto.add(channelListingUploadForm);
    }
}
