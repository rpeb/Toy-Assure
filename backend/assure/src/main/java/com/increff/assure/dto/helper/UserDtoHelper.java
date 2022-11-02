package com.increff.assure.dto.helper;

import com.increff.assure.exception.ApiException;
import com.increff.assure.model.UserData;
import com.increff.assure.model.UserForm;
import com.increff.assure.pojo.UserPojo;
import com.increff.assure.pojo.UserType;

import java.util.ArrayList;
import java.util.List;

public class UserDtoHelper {

    public static void validate(UserForm userForm) throws ApiException {
        if (userForm == null) {
            throw new ApiException("userform is null");
        }
        if (userForm.getName() == null || userForm.getName().isEmpty()) {
            throw new ApiException("user name cannot be empty");
        }
        if (userForm.getType() != UserType.CLIENT && userForm.getType() != UserType.CUSTOMER) {
            throw new ApiException("type of user can only be client or customer");
        }
    }

    public static void normalize(UserForm userForm) throws ApiException {
        validate(userForm);
        userForm.setName(userForm.getName().trim().toLowerCase());
    }
    public static UserData convertUserPojoToUserData(UserPojo p) {
        UserData d = new UserData();
        d.setId(p.getId());
        d.setName(p.getName());
        d.setType(p.getType());
        return d;
    }

    public static UserPojo convertUserFormtoUserPojo(UserForm userForm) {
        UserPojo p = new UserPojo();
        p.setName(userForm.getName());
        p.setType(userForm.getType());
        return p;
    }

    public static List<UserData> convertListOfUserPojoToListOfUserData(List<UserPojo> userPojoList) {
        List<UserData> userDataList = new ArrayList<>();
        for (UserPojo p: userPojoList) {
            userDataList.add(convertUserPojoToUserData(p));
        }
        return userDataList;
    }
}
