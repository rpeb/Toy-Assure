package com.increff.assure.dto.helper;

import com.increff.commons.exception.ApiException;

public class BinDtoHelper {
    public static void validate(Long numberOfBins) throws ApiException {
        if (numberOfBins < 0) {
            throw new ApiException("number of bins cannot be negative");
        }
    }
}
