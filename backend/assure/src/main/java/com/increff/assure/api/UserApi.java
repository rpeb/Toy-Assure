package com.increff.assure.api;

import com.increff.assure.dao.UserDao;
import com.increff.assure.pojo.UserPojo;
import com.increff.assure.pojo.UserType;
import com.increff.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserApi {
    @Autowired
    private UserDao userDao;


    @Transactional(readOnly = true)
    public List<UserPojo> getAllUsers() {
        return userDao.selectAll();
    }

    @Transactional
    public void updateUserName(UserPojo userPojo, String name) {
        userDao.updateUserName(userPojo, name);
    }

    @Transactional
    public void addUser(UserPojo userPojo) {
        userDao.insert(userPojo);
    }

    @Transactional(readOnly = true)
    public UserPojo getUserById(Long id) {
        return userDao.select(id);
    }

    @Transactional(readOnly = true)
    public void throwsIfInvalidClientId(Long clientId) throws ApiException {
        UserPojo userPojo = userDao.select(clientId);
        if (userPojo == null || userPojo.getType() != UserType.CLIENT) {
            throw new ApiException("invalid clientId: " + clientId);
        }
    }

    @Transactional(readOnly = true)
    public void throwsIfInvalidCustomerId(Long customerId) throws ApiException {
        UserPojo userPojo = userDao.select(customerId);
        if (userPojo == null || userPojo.getType() != UserType.CUSTOMER) {
            throw new ApiException("invalid customerId: " + customerId);
        }
    }
}
