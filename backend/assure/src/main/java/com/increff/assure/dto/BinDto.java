package com.increff.assure.dto;

import com.increff.assure.api.BinApi;
import com.increff.assure.dto.helper.BinDtoHelper;
import com.increff.assure.pojo.BinPojo;
import com.increff.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BinDto {

    @Autowired
    private BinApi binApi;

    @Transactional
    public void addBin(Long numberOfBins) throws ApiException {
        BinDtoHelper.validate(numberOfBins);
        for (int i = 1; i <= numberOfBins; ++i) {
            binApi.addBin(new BinPojo());
        }
    }

    @Transactional(readOnly = true)
    public List<BinPojo> getAll() {
        return binApi.getAll();
    }
}
