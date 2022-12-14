package com.increff.assure.api;

import com.increff.assure.dao.BinSkuDao;
import com.increff.assure.pojo.BinSkuPojo;
import com.increff.commons.exception.ApiException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BinSkuApi {

    public static final Logger LOGGER = LogManager.getLogger(BinSkuApi.class);

    @Autowired
    private BinSkuDao binSkuDao;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private BinApi binApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Transactional(readOnly = true)
    public BinSkuPojo getByBinIdAndGlobalSkuId(Long binId, Long globalSkuId) {
        return binSkuDao.select(binId, globalSkuId);
    }

    public List<BinSkuPojo> upload(List<BinSkuPojo> binSkuPojos) throws ApiException {
        List<BinSkuPojo> insertedBinSkus = new ArrayList<>();
        for (BinSkuPojo binSkuPojo : binSkuPojos) {
            BinSkuPojo exists = getByBinIdAndGlobalSkuId(
                    binSkuPojo.getBinId(),
                    binSkuPojo.getGlobalSkuId()
            );
            if (isNull(exists)) {
                binSkuDao.insert(binSkuPojo);
                insertedBinSkus.add(binSkuPojo);
            } else {
                exists.setQuantity(exists.getQuantity() + binSkuPojo.getQuantity());
                insertedBinSkus.add(exists);
            }
        }
        return insertedBinSkus;
    }

    @Transactional(readOnly = true)
    public List<BinSkuPojo> getAll() {
        return binSkuDao.select();
    }

    @Transactional(readOnly = true)
    public BinSkuPojo getById(Long id) {
        return binSkuDao.select(id);
    }

    @Transactional(readOnly = true)
    public List<BinSkuPojo> getByGlobalSkuId(Long globalSkuId) {
        return binSkuDao.selectByGlobalSkuId(globalSkuId);
    }

    public void updateQuantity(Long id, Long quantity) {
        binSkuDao.update(id, quantity);
    }
}
