package com.increff.assure.api;

import com.increff.assure.dao.BinDao;
import com.increff.assure.pojo.BinPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BinService {

    @Autowired
    private BinDao dao;

    @Transactional
    public void addBin(BinPojo p) {
        dao.insert(p);
    }

    @Transactional(readOnly = true)
    public List<BinPojo> getAll() {
        return dao.select();
    }
}
