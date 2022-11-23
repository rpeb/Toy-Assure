package com.increff.assure.controller;

import com.increff.assure.dto.UserDto;
import com.increff.assure.dto.helper.UserDtoHelper;
import com.increff.assure.model.data.UserData;
import com.increff.assure.model.form.UserForm;
import com.increff.commons.exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/user")
public class UserController {

    public static final Logger LOGGER = LogManager.getLogger(UserController.class);

    @Autowired
    private UserDto dto;

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "add a new user")
    public void addUser(@RequestBody UserForm userForm) throws ApiException {
        dto.addUser(userForm);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "get all the users")
    public List<UserData> getAllUsers() {
        return dto.getAllUsers();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "get a user by id")
    public UserData getUserById(@RequestParam Long id) {
        return dto.getUserById(id);
    }

    // todo: validations and normalizations

    @RequestMapping(method = RequestMethod.PATCH)
    @ApiOperation(value = "change the name of user")
    public void updateUserName(@RequestBody UserForm userForm, @RequestParam String name) {
        LOGGER.info("userform = " + userForm);
        LOGGER.info("name = " + name);
        dto.updateUserName(UserDtoHelper.convertUserFormtoUserPojo(userForm), name);
    }

}
