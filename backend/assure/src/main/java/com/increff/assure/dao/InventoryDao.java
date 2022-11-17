package com.increff.assure.dao;

import com.increff.assure.pojo.InventoryPojo;
import com.increff.commons.exception.ApiException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class InventoryDao extends AbstractDao {
    public static final String SELECT = "select p from InventoryPojo p";

    public static final String SELECT_BY_ID = "select p from InventoryPojo p where id=:id";

    public static final String SELECT_BY_GLOBAL_SKUID = "select p from InventoryPojo p " +
            "where globalSkuId=:globalSkuId";

    @Transactional(rollbackFor = ApiException.class)
    public void insert(InventoryPojo p) {
        em().persist(p);
    }

    @Transactional(readOnly = true)
    public List<InventoryPojo> select() {
        TypedQuery<InventoryPojo> query = em().createQuery(SELECT, InventoryPojo.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public InventoryPojo selectById(Long id) {
        TypedQuery<InventoryPojo> query = em().createQuery(SELECT_BY_ID, InventoryPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    @Transactional(readOnly = true)
    public InventoryPojo selectByGlobalSkuId(Long globalSkuId) {
        TypedQuery<InventoryPojo> query = em().createQuery(SELECT_BY_GLOBAL_SKUID, InventoryPojo.class);
        query.setParameter("globalSkuId", globalSkuId);
        return getSingle(query);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(InventoryPojo inventoryPojo, Long availableQuantity) {
        inventoryPojo.setAvailableQuantity(inventoryPojo.getAvailableQuantity() + availableQuantity);
    }
}
