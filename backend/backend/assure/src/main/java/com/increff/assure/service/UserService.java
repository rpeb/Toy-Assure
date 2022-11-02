package com.increff.assure.service;

import com.increff.assure.dao.UserDao;
import com.increff.assure.model.UserData;
import com.increff.assure.model.UserForm;
import com.increff.assure.pojo.UserPojo;
import com.increff.assure.pojo.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao dao;


    @Transactional
    public List<UserPojo> getAllUsers() {
        return dao.selectAll();
    }

    @Transactional
    public UserPojo updateUserName(UserPojo userPojo, String name) {
        return dao.updateUserName(userPojo, name);
    }

    @Transactional
    public void addUser(UserPojo userPojo) {
        dao.insert(userPojo);
    }

    public UserPojo getUserById(long id) {
        return dao.select(id);
    }
}
