package com.increff.assure.dto;

import com.increff.assure.api.BinApi;
import com.increff.assure.dto.helper.BinDtoHelper;
import com.increff.assure.model.data.BinData;
import com.increff.assure.pojo.BinPojo;
import com.increff.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BinDto {

    @Autowired
    private BinApi binApi;

    @Transactional
    public List<BinData> addBin(Long numberOfBins) throws ApiException {
        BinDtoHelper.validate(numberOfBins);
        List<BinData> addedBins = new ArrayList<>();
        for (int i = 1; i <= numberOfBins; ++i) {
            BinPojo pojo = binApi.addBin(new BinPojo());
            addedBins.add(BinDtoHelper.convertBinPojoToBinData(pojo));
        }
        return addedBins;
    }

    @Transactional(readOnly = true)
    public List<BinData> getAll() {
        return BinDtoHelper.convertListOfBinPojoToListOfBinData(binApi.getAll());
    }
}
