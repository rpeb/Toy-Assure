package com.increff.assure.dao;

import com.increff.assure.pojo.BinPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BinDao extends AbstractDao {

    public static final String SELECT = "select b from BinPojo b";
    public static final String SELECT_BY_ID = "select b from BinPojo b " +
            "where id=:id";

    @Transactional
    public void insert(BinPojo p) {
        em().persist(p);
    }

    @Transactional(readOnly = true)
    public List<BinPojo> select() {
        TypedQuery<BinPojo> query = em().createQuery(SELECT, BinPojo.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public BinPojo select(Long id) {
        TypedQuery<BinPojo> query = em().createQuery(SELECT_BY_ID, BinPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

}
