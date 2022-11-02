package com.increff.assure.dao;

import com.increff.assure.pojo.UserPojo;
import com.increff.assure.pojo.UserType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDao extends AbstractDao {

    private static final Logger LOGGER = LogManager.getLogger(UserDao.class);

    private static final String DELETE_ID = "delete from UserPojo p where id=:id";
    private static final String SELECT_BY_ID = "select p from UserPojo p where id=:id";
    private static final String SELECT_ALL = "select p from UserPojo p";
    private static final String SELECT_BY_NAME_AND_TYPE = "select p from UserPojo p" +
            "where name=:name and type=:type";


    @Transactional
    public void insert(UserPojo p) {
        em().persist(p);
    }


    @Transactional(readOnly = true)
    public UserPojo select(long id) {
        LOGGER.info("get by id called");
        TypedQuery<UserPojo> query = getQuery(SELECT_BY_ID, UserPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    @Transactional(readOnly = true)
    public UserPojo select(String name, UserType type) {
        TypedQuery<UserPojo> query = getQuery(SELECT_BY_NAME_AND_TYPE, UserPojo.class);
        query.setParameter("name", name);
        query.setParameter("type", type);
        return getSingle(query);
    }

    @Transactional(readOnly = true)
    public List<UserPojo> selectAll() {
        TypedQuery<UserPojo> query = getQuery(SELECT_ALL, UserPojo.class);
        return query.getResultList();
    }

    @Transactional
    public void updateUserName(UserPojo p, String name) {
        LOGGER.info("before select by name and type");
        UserPojo userPojo = select(p.getName(), p.getType());
        LOGGER.info(userPojo);
        userPojo.setName(name);
        //insert(userPojo);
    }

}
