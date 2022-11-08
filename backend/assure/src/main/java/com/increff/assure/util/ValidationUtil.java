package com.increff.assure.util;

import com.increff.assure.exception.ApiException;
import com.increff.assure.model.ErrorData;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class ValidationUtil {

    public static void throwsIfNotEmpty(List<ErrorData> errorDataList) throws ApiException {
        if (!CollectionUtils.isEmpty(errorDataList)) {
            throw new ApiException(errorDataList.toString());
        }
    }
}
