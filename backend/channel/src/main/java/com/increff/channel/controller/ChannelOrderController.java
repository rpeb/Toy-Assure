package com.increff.channel.controller;

import com.increff.channel.dto.ChannelOrderDto;
import com.increff.commons.exception.ApiException;
import com.increff.commons.model.data.ChannelInvoiceForm;
import com.increff.commons.model.form.ChannelOrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.TransformerException;
import java.io.IOException;

@Api
@RestController
@RequestMapping("/order")
public class ChannelOrderController {

    @Autowired
    private ChannelOrderDto dto;

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "submit an order")
    public void createOrder(@RequestBody ChannelOrderForm orderForm) throws ApiException {
        dto.createChannelOrder(orderForm);
    }

    @RequestMapping(value = "/invoice", method = RequestMethod.POST)
    @ApiOperation(value = "get invoice for a particular order")
    public byte[] getInvoice(@RequestBody ChannelInvoiceForm channelInvoiceForm) throws ApiException, IOException, TransformerException {
        return dto.getInvoice(channelInvoiceForm);
    }
}
