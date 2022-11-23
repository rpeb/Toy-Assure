package com.increff.assure.dto.helper;

import com.increff.assure.api.BinApi;
import com.increff.assure.api.BinSkuApi;
import com.increff.assure.api.ProductApi;
import com.increff.assure.api.UserApi;
import com.increff.assure.model.data.BinSkuData;
import com.increff.assure.model.data.ErrorData;
import com.increff.assure.model.form.BinSkuForm;
import com.increff.assure.model.form.BinSkuUpdateForm;
import com.increff.assure.pojo.BinPojo;
import com.increff.assure.pojo.BinSkuPojo;
import com.increff.assure.pojo.InventoryPojo;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.util.ValidationUtil;
import com.increff.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

@Service
public class BinSkuDtoHelper {

    @Autowired
    private UserApi userApi;

    @Autowired
    private BinApi binApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private BinSkuApi binSkuApi;

    public static void normalizeList(List<BinSkuForm> BinSkuFormList) throws ApiException {
        for (BinSkuForm f : BinSkuFormList) {
            normalize(f);
        }
    }

    public static void normalize(BinSkuForm f) throws ApiException {
        validate(f);
        f.setClientSkuId(f.getClientSkuId().trim().toLowerCase());
    }

    private static void validate(BinSkuForm BinSkuForm) throws ApiException {
        if (BinSkuForm == null) {
            throw new ApiException("sku entry cannot be empty");
        }
        if (BinSkuForm.getBinId() == null) {
            throw new ApiException("bin id cannot be empty");
        }
        if (BinSkuForm.getBinId() < 0) {
            throw new ApiException("bin id cannot be negative");
        }
        if (BinSkuForm.getClientSkuId() == null || BinSkuForm.getClientSkuId().isEmpty()) {
            throw new ApiException("client sku id cannot be empty");
        }
        if (BinSkuForm.getQuantity() == null) {
            throw new ApiException("sku quantity is missing");
        }
        if (BinSkuForm.getQuantity() < 0) {
            throw new ApiException("sku quantity cannot be negative");
        }
    }

    public static BinSkuPojo convertBinSkuFormToBinSkuPojo(BinSkuForm f, Long globalSkuId) throws ApiException {
        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setBinId(f.getBinId());
        binSkuPojo.setGlobalSkuId(globalSkuId);
        binSkuPojo.setQuantity(f.getQuantity());
        return binSkuPojo;
    }

    public static void checkDuplicateClientSkuIds(List<BinSkuForm> BinSkuFormList) throws ApiException {
        Set<String> clientSkuIds = new HashSet<>();
        for (BinSkuForm f : BinSkuFormList) {
            if (clientSkuIds.contains(f.getClientSkuId())) {
                throw new ApiException("duplicate client sku ids found");
            } else {
                clientSkuIds.add(f.getClientSkuId());
            }
        }
    }

    public static List<BinSkuPojo> convertListOfBinSkuFormToListOfBinSkuPojo(List<BinSkuForm> binSkuFormList, Map<String, Long> clientSkuIdToGlobalSkuIdMap) throws ApiException {
        List<BinSkuPojo> binSkuPojoList = new ArrayList<>();
        for (BinSkuForm binSkuForm : binSkuFormList) {
            Long globalSkuId = clientSkuIdToGlobalSkuIdMap.get(binSkuForm.getClientSkuId());
            binSkuPojoList.add(convertBinSkuFormToBinSkuPojo(binSkuForm, globalSkuId));
        }
        return binSkuPojoList;
    }

    public static List<InventoryPojo> convertListOfBinSkuFormToListOfInventoryPojo(List<BinSkuForm> binSkuFormList, Map<String, Long> clientSkuIdToGlobalSkuIdMap) {
        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        Map<String, Long> clientSkuIdToQuantityMap = binSkuFormList.stream().collect(groupingBy(BinSkuForm::getClientSkuId, summingLong(BinSkuForm::getQuantity)));
        for (String clientSkuId : clientSkuIdToQuantityMap.keySet()) {
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setAvailableQuantity(clientSkuIdToQuantityMap.get(clientSkuId));
            inventoryPojo.setGlobalSkuId(clientSkuIdToGlobalSkuIdMap.get(clientSkuId));
            inventoryPojoList.add(inventoryPojo);
        }
        return inventoryPojoList;
    }

    public static List<BinSkuData> convertListOfBinSkuPojoToListOfBinSkuData(List<BinSkuPojo> binSkuPojoList) {
        return binSkuPojoList
                .stream()
                .map(BinSkuDtoHelper::convertBinSkuPojoToBinSkuData)
                .collect(Collectors.toList());
    }

    private static BinSkuData convertBinSkuPojoToBinSkuData(BinSkuPojo binSkuPojo) {
        BinSkuData binSkuData = new BinSkuData();
        binSkuData.setBinId(binSkuPojo.getBinId());
        binSkuData.setQuantity(binSkuPojo.getQuantity());
        return binSkuData;
    }

    public static void validate(BinSkuUpdateForm binSkuUpdateForm) throws ApiException {
        if (isNull(binSkuUpdateForm)) {
            throw new ApiException("bin sku update form cannot be empty");
        }
        if (binSkuUpdateForm.getQuantity() == null) {
            throw new ApiException("quantity not present");
        }
        if (binSkuUpdateForm.getQuantity() < 0) {
            throw new ApiException("quantity cannot be negative");
        }
    }

    public ProductPojo getProductAndCheck(BinSkuUpdateForm binSkuUpdateForm) throws ApiException {
        ProductPojo productPojo = productApi.getByClientIdAndClientSkuId(
                binSkuUpdateForm.getClientId(),
                binSkuUpdateForm.getClientSkuId()
        );
        if (isNull(productPojo)) {
            throw new ApiException("invalid clientSkuId: " + binSkuUpdateForm.getClientSkuId());
        }
        return productPojo;
    }

    public BinSkuPojo convertBinSkuUpdateFormToBinSkuPojo(BinSkuUpdateForm binSkuUpdateForm) {
        BinSkuPojo pojo = new BinSkuPojo();
        pojo.setQuantity(binSkuUpdateForm.getQuantity());
        return pojo;
    }

    public void throwsIfInvalidBinId(List<BinSkuForm> binSkuFormList) throws ApiException {
        List<ErrorData> errorDataList = new ArrayList<>();
        Long row = 1L;
        for (BinSkuForm binSkuForm : binSkuFormList) {
            if (binApi.getBinById(binSkuForm.getBinId()) == null) {
                errorDataList.add(new ErrorData(row, "invalid bin id: " + binSkuForm.getBinId()));
            }
            row++;
        }
        ValidationUtil.throwsIfNotEmpty(errorDataList);
    }

    public Map<String, Long> getClientSkuIdToQuantityMap(List<BinSkuForm> binSkuFormList) throws ApiException {
        return binSkuFormList
                .stream()
                .collect(Collectors.groupingBy(BinSkuForm::getClientSkuId, Collectors.summingLong(BinSkuForm::getQuantity)));
    }

    public void throwsIfInvalidClientId(Long clientId) throws ApiException {
        userApi.throwsIfInvalidClientId(clientId);
    }

    public List<Long> getListofBinIds(List<BinSkuForm> binSkuFormList) {
        return binSkuFormList
                .stream()
                .map(BinSkuForm::getBinId)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getClientSkuIdToGlobalSkuIdMap(List<BinSkuForm> binSkuFormList, Long clientId) throws ApiException {
        List<String> clientSkuIdList = binSkuFormList.stream().map(BinSkuForm::getClientSkuId).collect(Collectors.toList());
        HashMap<String, Long> clientToGlobalSkuIdMap = new HashMap<>();
        for (String clientSkuId : clientSkuIdList) {
            ProductPojo productPojo = productApi.getByClientIdAndClientSkuId(clientId, clientSkuId);
            clientToGlobalSkuIdMap.put(productPojo.getClientSkuId(), productPojo.getGlobalSkuId());
        }
        return clientToGlobalSkuIdMap;
    }

    public void throwsIfInvalidClientSkuId(Long clientId, List<BinSkuForm> binSkuFormList) throws ApiException {
        Long row = 1L;
        List<ErrorData> errorDataList = new ArrayList<>();
        for (BinSkuForm binSkuItemForm : binSkuFormList) {
            if (isNull(productApi.getByClientIdAndClientSkuId(clientId, binSkuItemForm.getClientSkuId()))) {
                errorDataList.add(new ErrorData(row, "ClientSkuId: " + binSkuItemForm.getClientSkuId() + " does not exist"));
            }
            row++;
        }
        ValidationUtil.throwsIfNotEmpty(errorDataList);
    }

    public BinPojo getBinAndCheck(BinSkuUpdateForm binSkuUpdateForm) throws ApiException {
        BinPojo binPojo = binApi.getBinById(binSkuUpdateForm.getBinId());
        if (isNull(binPojo)) {
            throw new ApiException("invalid binid: " + binSkuUpdateForm.getBinId());
        }
        return binPojo;
    }

    public Map<Long, Long> getMapOfBinskuIdToChangeInQuantity(Long clientId, List<BinSkuForm> binSkuFormList) {
        Map<Long, Long> mapOfBinskuIdToChangeInQuantity = new HashMap<>();
        for (BinSkuForm binSkuForm: binSkuFormList) {
            Long globalSkuId = productApi.getByClientIdAndClientSkuId(
                    clientId,
                    binSkuForm.getClientSkuId()
            ).getGlobalSkuId();
            Long binSkuId = binSkuApi.getByBinIdAndGlobalSkuId(
                    binSkuForm.getBinId(),
                    globalSkuId
            ).getId();
            mapOfBinskuIdToChangeInQuantity.put(binSkuId, binSkuForm.getQuantity());
        }
        return mapOfBinskuIdToChangeInQuantity;
    }
}
