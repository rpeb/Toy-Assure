package com.increff.assure.api;

import com.increff.assure.dao.BinDao;
import com.increff.assure.pojo.BinPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BinApi {

    @Autowired
    private BinDao binDao;

    @Transactional
    public void addBin(BinPojo p) {
        binDao.insert(p);
    }

    @Transactional(readOnly = true)
    public List<BinPojo> getAll() {
        return binDao.select();
    }

    @Transactional(readOnly = true)
    public BinPojo getBinById(Long binId) {
        return binDao.select(binId);
    }
}
