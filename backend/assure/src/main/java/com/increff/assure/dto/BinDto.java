package com.increff.assure.dto;

import com.increff.assure.api.BinService;
import com.increff.assure.dto.helper.BinDtoHelper;
import com.increff.assure.exception.ApiException;
import com.increff.assure.pojo.BinPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BinDto {

    @Autowired
    private BinService service;

    @Transactional
    public void addBin(Long numberOfBins) throws ApiException {
        BinDtoHelper.validate(numberOfBins);
        for (int i = 1; i <= numberOfBins; ++i) {
            service.addBin(new BinPojo());
        }
    }

    @Transactional(readOnly = true)
    public List<BinPojo> getAll() {
        return service.getAll();
    }
}
