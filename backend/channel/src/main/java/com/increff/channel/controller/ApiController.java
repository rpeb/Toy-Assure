package com.increff.channel.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class ApiController {
    @RequestMapping(value = "/health", method = RequestMethod.GET)
    @ApiOperation(value = "health check endpoint")
    public String sayHello() {
        return "hello from channel module";
    }
}
