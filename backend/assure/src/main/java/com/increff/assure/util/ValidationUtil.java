package com.increff.assure.util;

import com.increff.assure.model.data.ErrorData;
import com.increff.commons.exception.ApiException;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class ValidationUtil {

    public static void throwsIfNotEmpty(List<ErrorData> errorDataList) throws ApiException {
        if (!CollectionUtils.isEmpty(errorDataList)) {
            throw new ApiException(errorDataList.toString());
        }
    }
}
