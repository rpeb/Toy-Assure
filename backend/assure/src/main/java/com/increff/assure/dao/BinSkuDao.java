package com.increff.assure.dao;

import com.increff.assure.pojo.BinSkuPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BinSkuDao extends AbstractDao {

    @Transactional
    public void insert(BinSkuPojo BinSkuPojo) {
        em().persist(BinSkuPojo);
    }

}
