package com.increff.assure.dao;

import com.increff.assure.model.form.ProductDetailsUpdateForm;
import com.increff.assure.pojo.ProductPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {

//    public static final Logger LOGGER = LogManager.getLogger(ProductDao.class);

    // TODO: Handle errors in product upload, check for clientId existence

    public static final String SELECT_ALL = "select p from ProductPojo p";
    public static final String SELECT_BY_GLOBALSKUID = "select p from ProductPojo p " +
            "where globalSkuId=:globalSkuId";

    public static final String SELECT_BY_CLIENTID = "select p from ProductPojo p " +
            "where clientId=:clientId";

    public static final String SELECT_BY_CLIENTID_AND_CLIENTSKUID = "select p from ProductPojo p " +
            "where clientId=:clientId and clientSkuId=:clientSkuId";

    @Transactional
    public void insert(ProductPojo p) {
        em().persist(p);
    }

    @Transactional(readOnly = true)
    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ALL, ProductPojo.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public ProductPojo select(Long globalSkuId) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_BY_GLOBALSKUID, ProductPojo.class);
        query.setParameter("globalSkuId", globalSkuId);
        return getSingle(query);
    }

    @Transactional(readOnly = true)
    public List<ProductPojo> selectByClientId(Long clientId) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_BY_CLIENTID, ProductPojo.class);
        query.setParameter("clientId", clientId);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public ProductPojo selectByClientIdAndClientSkuId(Long clientId, String clientSkuId) {
        TypedQuery<ProductPojo> query = getQuery(
                SELECT_BY_CLIENTID_AND_CLIENTSKUID,
                ProductPojo.class
        );
        query.setParameter("clientId", clientId);
        query.setParameter("clientSkuId", clientSkuId);
        return getSingle(query);
    }

    @Transactional
    public void update(ProductDetailsUpdateForm updateForm) {
        ProductPojo pojo = selectByClientIdAndClientSkuId(
                updateForm.getClientId(),
                updateForm.getClientSkuId()
        );
        pojo.setMrp(updateForm.getMrp());
        pojo.setName(updateForm.getName());
        pojo.setBrandId(updateForm.getBrandId());
        pojo.setDescription(updateForm.getDescription());
    }
}
