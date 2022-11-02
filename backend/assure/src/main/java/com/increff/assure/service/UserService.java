package com.increff.assure.service;

import com.increff.assure.dao.UserDao;
import com.increff.assure.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao dao;


    @Transactional(readOnly = true)
    public List<UserPojo> getAllUsers() {
        return dao.selectAll();
    }

    @Transactional
    public void updateUserName(UserPojo userPojo, String name) {
        dao.updateUserName(userPojo, name);
    }

    @Transactional
    public void addUser(UserPojo userPojo) {
        dao.insert(userPojo);
    }

    @Transactional(readOnly = true)
    public UserPojo getUserById(long id) {
        return dao.select(id);
    }
}
