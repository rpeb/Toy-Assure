package com.increff.assure.controller;

import com.increff.assure.dto.UserDto;
import com.increff.assure.dto.helper.UserDtoHelper;
import com.increff.assure.exception.ApiException;
import com.increff.assure.model.UserData;
import com.increff.assure.model.UserForm;
import com.increff.assure.pojo.UserType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Api
@RestController
@RequestMapping("/api/user")
public class UserController {

    public static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserDto dto;

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "add a new user")
    public ResponseEntity<Object> addUser(@RequestBody UserForm userForm) {
        try {
            dto.addUser(userForm);
            return ResponseEntity.created(URI.create("xyz")).build();
        } catch (ApiException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "get all the users")
    public List<UserData> getAllUsers() {
        return dto.getAllUsers();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "get a user by id")
    public UserData getUserById(@RequestParam long id) {
        return dto.getUserById(id);
    }

    @RequestMapping(method = RequestMethod.PATCH)
    @ApiOperation(value = "change the name of user")
    public UserData updateUserName(@RequestBody UserForm userForm, @RequestParam String name) {
        logger.info("userform = " + userForm);
        logger.info("name = " + name);
        return dto.updateUserName(UserDtoHelper.convertUserFormtoUserPojo(userForm), name);
    }

}
