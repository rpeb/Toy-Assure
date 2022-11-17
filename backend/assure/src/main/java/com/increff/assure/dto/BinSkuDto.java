package com.increff.assure.dto;

import com.increff.assure.api.BinSkuApi;
import com.increff.assure.api.InventoryApi;
import com.increff.assure.dto.helper.BinSkuDtoHelper;
import com.increff.assure.model.data.BinSkuData;
import com.increff.assure.model.form.BinSkuForm;
import com.increff.assure.model.form.BinSkuUpdateForm;
import com.increff.assure.pojo.BinSkuPojo;
import com.increff.assure.pojo.InventoryPojo;
import com.increff.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class BinSkuDto {

    @Autowired
    private BinSkuApi binSkuApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private BinSkuDtoHelper binSkuDtoHelper;

    @Transactional(rollbackFor = ApiException.class)
    public void upload(List<BinSkuForm> binSkuFormList, Long clientId) throws ApiException {
        BinSkuDtoHelper.normalizeList(binSkuFormList);
        BinSkuDtoHelper.checkDuplicateClientSkuIds(binSkuFormList);
        binSkuDtoHelper.throwsIfInvalidBinId(binSkuFormList);
        binSkuDtoHelper.throwsIfInvalidClientId(clientId);
        Map<String, Long> clientSkuIdToGlobalSkuIdMap = binSkuDtoHelper
                .getClientSkuIdToGlobalSkuIdMap(binSkuFormList, clientId);
        BinSkuDtoHelper.throwsIfInvalidClientSkuId(clientSkuIdToGlobalSkuIdMap, binSkuFormList);
//        Map<String, Long> clientSkuIdToQuantityMap = binSkuDtoHelper
//                .getClientSkuIdToQuantityMap(binSkuFormList);
        List<BinSkuPojo> binSkuPojos = BinSkuDtoHelper
                .convertListOfBinSkuFormToListOfBinSkuPojo(
                        binSkuFormList,
                        clientSkuIdToGlobalSkuIdMap
                );
        binSkuApi.upload(binSkuPojos);
        List<InventoryPojo> inventoryPojos = BinSkuDtoHelper
                .convertListOfBinSkuFormToListOfInventoryPojo(
                        binSkuFormList,
                        clientSkuIdToGlobalSkuIdMap
                );
        inventoryApi.upload(inventoryPojos);
    }

    @Transactional(readOnly = true)
    public List<BinSkuData> getAll() {
        return BinSkuDtoHelper.convertListOfBinSkuPojoToListOfBinSkuData(binSkuApi.getAll());
    }

    public void updateQuantity(Long id, BinSkuUpdateForm binSkuUpdateForm) throws ApiException {
        BinSkuDtoHelper.validate(binSkuUpdateForm);
        binSkuApi.updateQuantity(id, binSkuDtoHelper.convertBinSkuUpdateFormToBinSkuPojo(binSkuUpdateForm));
    }
}
