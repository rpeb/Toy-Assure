package com.increff.assure.dto.helper;

import com.increff.assure.model.data.BinData;
import com.increff.assure.pojo.BinPojo;
import com.increff.commons.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

public class BinDtoHelper {
    public static void validate(Long numberOfBins) throws ApiException {
        if (numberOfBins < 0) {
            throw new ApiException("number of bins cannot be negative");
        }
    }

    public static List<BinData> convertListOfBinPojoToListOfBinData(List<BinPojo> binPojos) {
        List<BinData> binDataList = new ArrayList<>();
        for (BinPojo binPojo: binPojos) {
            binDataList.add(convertBinPojoToBinData(binPojo));
        }
        return binDataList;
    }

    public static BinData convertBinPojoToBinData(BinPojo binPojo) {
        BinData data = new BinData();
        data.setId(binPojo.getId());
        return data;
    }
}
