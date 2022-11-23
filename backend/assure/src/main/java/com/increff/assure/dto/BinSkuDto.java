package com.increff.assure.dto;

import com.increff.assure.api.BinSkuApi;
import com.increff.assure.api.ProductApi;
import com.increff.assure.dto.helper.BinSkuDtoHelper;
import com.increff.assure.model.data.BinSkuData;
import com.increff.assure.model.form.BinSkuForm;
import com.increff.assure.model.form.BinSkuUpdateForm;
import com.increff.assure.pojo.BinPojo;
import com.increff.assure.pojo.BinSkuPojo;
import com.increff.assure.pojo.ProductPojo;
import com.increff.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.min;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BinSkuDto {

    @Autowired
    private BinSkuApi binSkuApi;

    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private BinSkuDtoHelper binSkuDtoHelper;

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
        Map<Long, Long> mapOfBinskuIdToChangeInQuantity = binSkuDtoHelper
                .getMapOfBinskuIdToChangeInQuantity(clientId, binSkuFormList);
        inventoryDto.updateInventory(uploadedBinSkus, mapOfBinskuIdToChangeInQuantity);
    }

    @Transactional(readOnly = true)
    public List<BinSkuData> getAll() {
        return BinSkuDtoHelper.convertListOfBinSkuPojoToListOfBinSkuData(binSkuApi.getAll());
    }

    public void updateQuantity(BinSkuUpdateForm binSkuUpdateForm) throws ApiException {
        BinSkuDtoHelper.validate(binSkuUpdateForm);
        binSkuDtoHelper.throwsIfInvalidClientId(binSkuUpdateForm.getClientId());
        BinPojo binPojo = binSkuDtoHelper.getBinAndCheck(binSkuUpdateForm);
        ProductPojo productPojo = binSkuDtoHelper .getProductAndCheck(binSkuUpdateForm);
        BinSkuPojo binSkuPojo = binSkuApi.getByBinIdAndGlobalSkuId(
                binPojo.getId(),
                productPojo.getGlobalSkuId()
        );
        Long change = binSkuUpdateForm.getQuantity() - binSkuPojo.getQuantity();
        binSkuApi.updateQuantity(binSkuPojo.getId(), binSkuUpdateForm.getQuantity());
        Long globalSkuId = binSkuApi.getById(binSkuPojo.getId()).getGlobalSkuId();
        inventoryDto.updateAvailableQuantity(globalSkuId, change);
    }

    public void updateAvailableQuantity(Long globalSkuId, Long quantity) {
        List<BinSkuPojo> binSkuPojos = binSkuApi.getByGlobalSkuId(globalSkuId);
        Collections.sort(binSkuPojos, Comparator.comparing(BinSkuPojo::getQuantity));
        Collections.reverse(binSkuPojos);

        for (BinSkuPojo binSkuPojo : binSkuPojos) {
            Long allocatedQtyInBin = min(quantity, binSkuPojo.getQuantity());
            binSkuPojo.setQuantity(binSkuPojo.getQuantity() - allocatedQtyInBin);
            quantity -= allocatedQtyInBin;

            if (quantity <= 0)
                break;
        }
    }

    public void setAvailableQuantityToZero(Long globalSkuId) {
        List<BinSkuPojo> binSkuPojos = binSkuApi.getByGlobalSkuId(globalSkuId);
        for (BinSkuPojo binSkuPojo : binSkuPojos) {
            binSkuPojo.setQuantity(0L);
        }
    }
}
