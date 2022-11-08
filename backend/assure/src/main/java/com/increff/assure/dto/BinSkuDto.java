package com.increff.assure.dto;

import com.increff.assure.api.BinSkuService;
import com.increff.assure.dto.helper.BinSkuDtoHelper;
import com.increff.assure.exception.ApiException;
import com.increff.assure.model.BinSkuForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BinSkuDto {

    @Autowired
    private BinSkuService service;

    @Autowired
    private BinSkuDtoHelper binSkuDtoHelper;

    @Transactional
    public void upload(List<BinSkuForm> BinSkuFormList, Long clientId) throws ApiException {
        BinSkuDtoHelper.normalizeList(BinSkuFormList);
        BinSkuDtoHelper.checkDuplicateClientSkuIds(BinSkuFormList);
        binSkuDtoHelper.checkIfClientExists(clientId);
        service.upload(BinSkuFormList, clientId);
    }
}
