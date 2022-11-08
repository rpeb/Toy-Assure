package com.increff.assure.dto.helper;

import com.increff.assure.api.UserService;
import com.increff.assure.exception.ApiException;
import com.increff.assure.model.BinSkuForm;
import com.increff.assure.pojo.BinSkuPojo;
import com.increff.assure.pojo.UserPojo;
import com.increff.assure.pojo.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BinSkuDtoHelper {

    @Autowired
    private UserService userService;

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
//        normalize(f);
        BinSkuPojo p = new BinSkuPojo();
        p.setBinId(f.getBinId());
        p.setQuantity(f.getQuantity());
        p.setGlobalSkuId(globalSkuId);
        return p;
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

    public void checkIfClientExists(Long clientId) throws ApiException {
        UserPojo userPojo = userService.getUserById(clientId);
        if (userPojo == null || userPojo.getType() != UserType.CLIENT) {
            throw new ApiException("invalid clientId: " + clientId);
        }
    }
}
