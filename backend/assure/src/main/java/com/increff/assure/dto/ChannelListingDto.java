package com.increff.assure.dto;

import com.increff.assure.api.ChannelListingService;
import com.increff.assure.api.ChannelService;
import com.increff.assure.api.ProductService;
import com.increff.assure.api.UserService;
import com.increff.assure.exception.ApiException;
import com.increff.assure.model.ChannelListingForm;
import com.increff.assure.model.ChannelListingUploadForm;
import com.increff.assure.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChannelListingDto {
    @Autowired
    private ChannelListingService service;

    @Autowired
    private ProductService productService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private UserService userService;

    @Transactional(rollbackFor = ApiException.class)
    public void add(ChannelListingUploadForm channelListingUploadForm) throws ApiException {
        validateAndNormalizeList(channelListingUploadForm);
        throwsIfInvalidChannelId(channelListingUploadForm.getChannelId());
        throwsIfInvalidClientId(channelListingUploadForm.getClientId());
        List<ChannelListingPojo> channelListingPojos = convertChannelListingUploadFormToChannelListingPojoList(channelListingUploadForm);
        service.add(channelListingPojos);
    }

    private void throwsIfInvalidClientId(Long clientId) throws ApiException {
        UserPojo userPojo = userService.getUserById(clientId);
        if (userPojo == null || userPojo.getType() != UserType.CLIENT) {
            throw new ApiException("invalid client id");
        }
    }

    private void throwsIfInvalidChannelId(Long channelId) throws ApiException {
        ChannelPojo channelPojo = channelService.get(channelId);
        if (channelPojo == null) {
            throw new ApiException("invalid channel id");
        }
    }

    private void validateAndNormalizeList(ChannelListingUploadForm channelListingUploadForm) throws ApiException {
        if (channelListingUploadForm == null) {
            throw new ApiException("upload details not found");
        }
        if (channelListingUploadForm.getClientId() == null) {
            throw new ApiException("client id is empty");
        }
        if (channelListingUploadForm.getChannelId() == null) {
            throw new ApiException("channel id is empty");
        }
        if (CollectionUtils.isEmpty(channelListingUploadForm.getChannelListingFormList())) {
            throw new ApiException("channel list is empty");
        }
        for (ChannelListingForm channelListingForm: channelListingUploadForm.getChannelListingFormList()) {
            normalize(channelListingForm);
        }
    }

    private void normalize(ChannelListingForm channelListingForm) {
        channelListingForm.setChannelSkuId(channelListingForm.getChannelSkuId().trim().toLowerCase());
        channelListingForm.setClientSkuId(channelListingForm.getClientSkuId().trim().toLowerCase());
    }

    public List<ChannelListingPojo> convertChannelListingUploadFormToChannelListingPojoList(ChannelListingUploadForm channelListingUploadForm) throws ApiException {
        List<ChannelListingPojo> channelListingPojos = new ArrayList<>();
        Long channelId = channelListingUploadForm.getChannelId();
        Long clientId = channelListingUploadForm.getClientId();
        List<ChannelListingForm> channelListingFormList = channelListingUploadForm.getChannelListingFormList();
        for (ChannelListingForm channelListingForm : channelListingFormList) {
            channelListingPojos.add(convertChannelListingFormToChannelListingPojo(channelListingForm, clientId, channelId));
        }
        return channelListingPojos;
    }

    private ChannelListingPojo convertChannelListingFormToChannelListingPojo(ChannelListingForm channelListingForm, Long channelId, Long clientId) throws ApiException {
        ChannelListingPojo channelListingPojo = new ChannelListingPojo();
        channelListingPojo.setChannelId(channelId);
        channelListingPojo.setClientId(clientId);
        channelListingPojo.setChannelSkuId(channelListingForm.getChannelSkuId());
        channelListingPojo.setGlobalSkuId(getGlobalSkuId(clientId, channelListingForm.getClientSkuId()));
        return channelListingPojo;
    }

    @Transactional(rollbackFor = ApiException.class)
    private Long getGlobalSkuId(Long clientId, String clientSkuId) throws ApiException {
        ProductPojo productPojo = productService.getByClientIdAndClientSkuId(clientId, clientSkuId);
        return productPojo.getGlobalSkuId();
    }
}
