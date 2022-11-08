package com.increff.assure.api;

import com.increff.assure.dao.BinSkuDao;
import com.increff.assure.dto.helper.BinSkuDtoHelper;
import com.increff.assure.exception.ApiException;
import com.increff.assure.model.BinSkuForm;
import com.increff.assure.pojo.BinPojo;
import com.increff.assure.pojo.BinSkuPojo;
import com.increff.assure.pojo.InventoryPojo;
import com.increff.assure.pojo.ProductPojo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BinSkuService {

    public static final Logger LOGGER = LogManager.getLogger(BinSkuService.class);

    @Autowired
    private BinSkuDao dao;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BinService binService;

    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public void upload(List<BinSkuForm> binSkuFormList, Long clientId) throws ApiException {

        List<ProductPojo> productPojos = productService.getByClientId(clientId);
        Set<String> clientSkuIds = productPojos
                .stream()
                .map(ProductPojo::getClientSkuId)
                .collect(Collectors.toSet());
        List<BinPojo> binPojos = binService.getAll();
        Set<Long> binIds = binPojos.stream().map(BinPojo::getId).collect(Collectors.toSet());

        for (BinSkuForm binSkuForm : binSkuFormList) {
            if (!clientSkuIds.contains(binSkuForm.getClientSkuId())) {
                throw new ApiException("invalid client sku id: " + binSkuForm.getClientSkuId());
            }
            if (!binIds.contains(binSkuForm.getBinId())) {
                throw new ApiException("invalid bin id: " + binSkuForm.getBinId());
            }
            ProductPojo productPojo = productService
                    .getByClientIdAndClientSkuId(clientId, binSkuForm.getClientSkuId());
            Long globalSkuId = productPojo.getGlobalSkuId();
            LOGGER.info("globalskuid = " + globalSkuId);
            BinSkuPojo BinSkuPojo = BinSkuDtoHelper
                    .convertBinSkuFormToBinSkuPojo(binSkuForm, globalSkuId);

            dao.insert(BinSkuPojo);

            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setGlobalSkuId(globalSkuId);
            inventoryPojo.setAvailableQuantity(binSkuForm.getQuantity());
            inventoryService.add(inventoryPojo);

//            inventoryService.updateAvailableQuantity(globalSkuId, BinSkuForm.getQuantity());

        }
    }
}
