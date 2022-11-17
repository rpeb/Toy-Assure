package com.increff.assure.controller;

import com.increff.assure.dto.OrderDto;
import com.increff.assure.model.form.InternalOrderForm;
import com.increff.commons.exception.ApiException;
import com.increff.commons.model.form.ChannelOrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.TransformerException;
import java.io.IOException;

@Api
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderDto dto;

    @RequestMapping(value = "/internal", method = RequestMethod.POST)
    @ApiOperation(value = "create internal order")
    public void createInternalOrder(@RequestBody InternalOrderForm orderForm) throws ApiException {
        dto.createInternalOrder(orderForm);
    }

    @RequestMapping(value = "/channel", method = RequestMethod.POST)
    @ApiOperation(value = "create channel order")
    public void createChannelOrder(@RequestBody ChannelOrderForm orderForm) throws ApiException {
        dto.createChannelOrder(orderForm);
    }

    @RequestMapping(value = "/invoice/{orderId}", method = RequestMethod.GET)
    @ApiOperation(value = "get invoice for a particular order")
    public String getInvoice(@PathVariable Long orderId) throws ApiException, IOException, TransformerException {
        return dto.getInvoice(orderId);
    }
}
