package com.increff.assure.dao;

import com.increff.assure.pojo.UserPojo;
import com.increff.assure.pojo.UserType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class UserDao extends AbstractDao {

    private static final Logger logger = LogManager.getLogger(UserDao.class);

    private static final String delete_id = "delete from UserPojo p where id=:id";
    private static final String select_id = "select p from UserPojo p where id=:id";
    private static final String select_all = "select p from UserPojo p";
    private static final String select_name_type = "select p from UserPojo p where name=:name and type=:type";


    @Transactional
    public void insert(UserPojo p) {
        em().persist(p);
    }

    public int delete(long id) {
        Query query = em().createQuery(delete_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public UserPojo select(long id) {
        logger.info("get by id called");
        TypedQuery<UserPojo> query = getQuery(select_id, UserPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public UserPojo select(String name, UserType type) {
        TypedQuery<UserPojo> query = getQuery(select_name_type, UserPojo.class);
        query.setParameter("name", name);
        query.setParameter("type", type);
        return getSingle(query);
    }

    public List<UserPojo> selectAll() {
        TypedQuery<UserPojo> query = getQuery(select_all, UserPojo.class);
        return query.getResultList();
    }

    @Transactional
    public UserPojo updateUserName(UserPojo p, String name) {
        logger.info("before select by name and type");
        UserPojo userPojo = select(p.getName(), p.getType());
        logger.info(userPojo);
        userPojo.setName(name);
        insert(userPojo);
        return userPojo;
    }

}
