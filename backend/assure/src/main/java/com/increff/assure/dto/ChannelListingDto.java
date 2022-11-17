package com.increff.assure.dto;

import com.increff.assure.api.ChannelApi;
import com.increff.assure.api.ChannelListingApi;
import com.increff.assure.api.ProductApi;
import com.increff.assure.api.UserApi;
import com.increff.assure.model.form.ChannelListingForm;
import com.increff.assure.model.form.ChannelListingUploadForm;
import com.increff.assure.pojo.ChannelListingPojo;
import com.increff.assure.pojo.ChannelPojo;
import com.increff.assure.pojo.ProductPojo;
import com.increff.commons.exception.ApiException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

@Service
public class ChannelListingDto {

    public static final Logger LOGGER = LogManager.getLogger(ChannelListingDto.class);

    @Autowired
    private ChannelListingApi channelListingApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private ChannelApi channelApi;

    @Autowired
    private UserApi userApi;

    @Transactional(rollbackFor = ApiException.class)
    public void add(ChannelListingUploadForm channelListingUploadForm) throws ApiException {
        validateAndNormalizeList(channelListingUploadForm);
        throwsIfInvalidChannelId(channelListingUploadForm.getChannelId());
        userApi.throwsIfInvalidClientId(channelListingUploadForm.getClientId());
        List<ChannelListingPojo> channelListingPojos = convertChannelListingUploadFormToChannelListingPojoList(channelListingUploadForm);
        channelListingApi.add(channelListingPojos);
    }

    private void throwsIfInvalidChannelId(Long channelId) throws ApiException {
        ChannelPojo channelPojo = channelApi.get(channelId);
        if (isNull(channelPojo) ||
                Objects.equals(channelPojo.getId(), channelApi.getInternalChannelInfo().getId())) {
            throw new ApiException("invalid channel id: " + channelId);
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
        for (ChannelListingForm channelListingForm : channelListingUploadForm.getChannelListingFormList()) {
            normalize(channelListingForm);
        }
    }

    private void normalize(ChannelListingForm channelListingForm) {
        channelListingForm.setChannelSkuId(channelListingForm.getChannelSkuId().trim().toLowerCase());
        channelListingForm.setClientSkuId(channelListingForm.getClientSkuId().trim().toLowerCase());
//        LOGGER.info("channelListingForm = " + channelListingForm);
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

    private ChannelListingPojo convertChannelListingFormToChannelListingPojo(ChannelListingForm channelListingForm, Long clientId, Long channelId) throws ApiException {
        ChannelListingPojo channelListingPojo = new ChannelListingPojo();
        channelListingPojo.setClientId(clientId);
        channelListingPojo.setChannelId(channelId);
        channelListingPojo.setChannelSkuId(channelListingForm.getChannelSkuId());
        channelListingPojo.setGlobalSkuId(getGlobalSkuId(clientId, channelListingForm.getClientSkuId()));
        return channelListingPojo;
    }

    @Transactional(rollbackFor = ApiException.class)
    private Long getGlobalSkuId(Long clientId, String clientSkuId) throws ApiException {
        ProductPojo productPojo = productApi.getByClientIdAndClientSkuId(clientId, clientSkuId);
        return productPojo.getGlobalSkuId();
    }
}
