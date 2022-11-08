package com.increff.assure.dto;

import com.increff.assure.api.UserService;
import com.increff.assure.dto.helper.UserDtoHelper;
import com.increff.assure.exception.ApiException;
import com.increff.assure.model.UserData;
import com.increff.assure.model.UserForm;
import com.increff.assure.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserDto {


    @Autowired
    private UserService service;

    @Transactional(readOnly = true)
    public List<UserData> getAllUsers() {
        return UserDtoHelper.convertListOfUserPojoToListOfUserData(service.getAllUsers());
    }

    @Transactional
    public void addUser(UserForm userForm) throws ApiException {
        UserDtoHelper.normalize(userForm);
        UserPojo userPojo = UserDtoHelper.convertUserFormtoUserPojo(userForm);
        service.addUser(userPojo);
    }

    @Transactional(readOnly = true)
    public UserData getUserById(Long id) {
        return UserDtoHelper.convertUserPojoToUserData(service.getUserById(id));
    }

    @Transactional
    public void updateUserName(UserPojo userPojo, String name) {
        service.updateUserName(userPojo, name);
    }
}
