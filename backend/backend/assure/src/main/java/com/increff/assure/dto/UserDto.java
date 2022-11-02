package com.increff.assure.dto;

import com.increff.assure.dto.helper.UserDtoHelper;
import com.increff.assure.exception.ApiException;
import com.increff.assure.model.UserData;
import com.increff.assure.model.UserForm;
import com.increff.assure.pojo.UserPojo;
import com.increff.assure.pojo.UserType;
import com.increff.assure.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class UserDto {

    @Autowired
    private UserService service;

    public List<UserData> getAllUsers() {
        return UserDtoHelper.convertListOfUserPojoToListOfUserData(service.getAllUsers());
    }

    @Transactional
    public void addUser(UserForm userForm) throws ApiException {
        UserDtoHelper.normalize(userForm);
        UserPojo userPojo = UserDtoHelper.convertUserFormtoUserPojo(userForm);
        service.addUser(userPojo);
    }

    public UserData getUserById(long id) {
        return UserDtoHelper.convertUserPojoToUserData(service.getUserById(id));
    }

    @Transactional
    public UserData updateUserName(UserPojo userPojo, String name) {
        return UserDtoHelper.convertUserPojoToUserData(service.updateUserName(userPojo, name));
    }
}
