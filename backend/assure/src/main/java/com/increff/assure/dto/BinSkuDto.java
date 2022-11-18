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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class BinSkuDto {

    @Autowired
    private BinSkuApi binSkuApi;

    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private BinSkuDtoHelper binSkuDtoHelper;

    @Transactional(rollbackFor = ApiException.class)
    public void upload(List<BinSkuForm> binSkuFormList, Long clientId) throws ApiException {
        BinSkuDtoHelper.normalizeList(binSkuFormList);
        BinSkuDtoHelper.checkDuplicateClientSkuIds(binSkuFormList);
        binSkuDtoHelper.throwsIfInvalidBinId(binSkuFormList);
        binSkuDtoHelper.throwsIfInvalidClientId(clientId);
        binSkuDtoHelper.throwsIfInvalidClientSkuId(clientId, binSkuFormList);
        Map<String, Long> clientSkuIdToGlobalSkuIdMap = binSkuDtoHelper
                .getClientSkuIdToGlobalSkuIdMap(binSkuFormList, clientId);
        List<BinSkuPojo> binSkuPojos = BinSkuDtoHelper
                .convertListOfBinSkuFormToListOfBinSkuPojo(
                        binSkuFormList,
                        clientSkuIdToGlobalSkuIdMap
                );
        List<BinSkuPojo> uploadedBinSkus = binSkuApi.upload(binSkuPojos);
        inventoryDto.updateInventory(uploadedBinSkus);
    }

    @Transactional(readOnly = true)
    public List<BinSkuData> getAll() {
        return BinSkuDtoHelper.convertListOfBinSkuPojoToListOfBinSkuData(binSkuApi.getAll());
    }

    public void updateQuantity(Long id, BinSkuUpdateForm binSkuUpdateForm) throws ApiException {
        BinSkuDtoHelper.validate(binSkuUpdateForm);
        binSkuApi.updateQuantity(id, binSkuDtoHelper.convertBinSkuUpdateFormToBinSkuPojo(binSkuUpdateForm).getQuantity());
    }

    public void updateAvailableQuantity(Long globalSkuId, Long quantity) {
        List<BinSkuPojo> binSkuPojos = binSkuApi.getByGlobalSkuId(globalSkuId);
        Collections.sort(binSkuPojos, Comparator.comparing(BinSkuPojo::getQuantity));
        Collections.reverse(binSkuPojos);
        int i = 0;
        while (quantity >= 0) {
            BinSkuPojo binSkuPojo = binSkuPojos.get(i);
            if (binSkuPojo.getQuantity() <= quantity) {
                quantity -= binSkuPojo.getQuantity();
                binSkuPojo.setQuantity(0L);
            } else {
                binSkuPojo.setQuantity(binSkuPojo.getQuantity() - quantity);
                quantity = 0L;
            }
            i++;
            if (i == binSkuPojos.size()) {
                break;
            }
        }
    }
}
