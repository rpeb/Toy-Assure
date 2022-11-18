package com.increff.assure.dao;

import com.increff.assure.pojo.BinSkuPojo;
import com.increff.commons.exception.ApiException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BinSkuDao extends AbstractDao {

    public static final String SELECT = "select b from BinSkuPojo b";
    public static final String SELECT_BY_BINID_GLOBALSKUID = "select b from BinSkuPojo b " +
            "where binId=:binId and globalSkuId=:globalSkuId";

    public static final String SELECT_BY_GLOBALSKUID = "select b from BinSkuPojo b " +
            "where globalSkuId=:globalSkuId";

    public static final String SELECT_BY_ID = "select b from BinSkuPojo b where id=:id";

    @Transactional
    public void insert(BinSkuPojo BinSkuPojo) {
        em().persist(BinSkuPojo);
    }

    @Transactional(readOnly = true)
    public BinSkuPojo select(Long binId, Long globalSkuId) {
        TypedQuery<BinSkuPojo> query = em().createQuery(
                SELECT_BY_BINID_GLOBALSKUID,
                BinSkuPojo.class
        );
        query.setParameter("binId", binId);
        query.setParameter("globalSkuId", globalSkuId);
        return getSingle(query);
    }

    @Transactional(readOnly = true)
    public List<BinSkuPojo> select() {
        TypedQuery<BinSkuPojo> query = em().createQuery(SELECT, BinSkuPojo.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public List<BinSkuPojo> selectByGlobalSkuId(Long globalSkuId) {
        TypedQuery<BinSkuPojo> query = em().createQuery(SELECT_BY_GLOBALSKUID, BinSkuPojo.class);
        query.setParameter("globalSkuId", globalSkuId);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public BinSkuPojo select(Long id) {
        TypedQuery<BinSkuPojo> query = em().createQuery(
                SELECT_BY_ID,
                BinSkuPojo.class
        );
        query.setParameter("id", id);
        return getSingle(query);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(Long id, Long quantity) {
        BinSkuPojo exists = select(id);
        exists.setQuantity(quantity);
    }
}
