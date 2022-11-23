package com.increff.assure.dto;

import com.increff.assure.api.UserApi;
import com.increff.assure.dto.helper.UserDtoHelper;
import com.increff.assure.model.data.UserData;
import com.increff.assure.model.form.UserForm;
import com.increff.assure.pojo.UserPojo;
import com.increff.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserDto {


    @Autowired
    private UserApi userApi;

    @Transactional(readOnly = true)
    public List<UserData> getAllUsers() {
        return UserDtoHelper.convertListOfUserPojoToListOfUserData(userApi.getAllUsers());
    }

    @Transactional
    public void addUser(UserForm userForm) throws ApiException {
        UserDtoHelper.normalize(userForm);
        UserPojo userPojo = UserDtoHelper.convertUserFormtoUserPojo(userForm);
        userApi.addUser(userPojo);
    }

    @Transactional(readOnly = true)
    public UserData getUserById(Long id) {
        return UserDtoHelper.convertUserPojoToUserData(userApi.getUserById(id));
    }

    @Transactional
    public void updateUserName(UserPojo userPojo, String name) {
        name = name.trim().toLowerCase();
        userApi.updateUserName(userPojo, name);
    }
}
