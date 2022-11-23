package com.increff.assure.dto.helper;

import com.increff.assure.api.ChannelApi;
import com.increff.assure.api.ChannelListingApi;
import com.increff.assure.api.ProductApi;
import com.increff.assure.model.data.ErrorData;
import com.increff.assure.model.form.ChannelListingForm;
import com.increff.assure.model.form.ChannelListingUploadForm;
import com.increff.assure.pojo.ChannelListingPojo;
import com.increff.assure.pojo.ChannelPojo;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.util.ValidationUtil;
import com.increff.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ChannelListingDtoHelper {

    @Autowired
    private ProductApi productApi;

    @Autowired
    private ChannelApi channelApi;

    @Autowired
    private ChannelListingApi channelListingApi;

    public static void validateAndNormalizeList(ChannelListingUploadForm channelListingUploadForm) throws ApiException {
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

    private static void normalize(ChannelListingForm channelListingForm) {
        channelListingForm.setChannelSkuId(channelListingForm.getChannelSkuId().trim().toLowerCase());
        channelListingForm.setClientSkuId(channelListingForm.getClientSkuId().trim().toLowerCase());
    }

    public List<ChannelListingPojo> convertChannelListingUploadFormToChannelListingPojoList(ChannelListingUploadForm channelListingUploadForm) {
        List<ChannelListingPojo> channelListingPojos = new ArrayList<>();
        Long channelId = channelListingUploadForm.getChannelId();
        Long clientId = channelListingUploadForm.getClientId();
        List<ChannelListingForm> channelListingFormList = channelListingUploadForm.getChannelListingFormList();
        for (ChannelListingForm channelListingForm : channelListingFormList) {
            channelListingPojos.add(convertChannelListingFormToChannelListingPojo(channelListingForm, clientId, channelId));
        }
        return channelListingPojos;
    }

    private ChannelListingPojo convertChannelListingFormToChannelListingPojo(ChannelListingForm channelListingForm, Long clientId, Long channelId) {
        ChannelListingPojo channelListingPojo = new ChannelListingPojo();
        channelListingPojo.setClientId(clientId);
        channelListingPojo.setChannelId(channelId);
        channelListingPojo.setChannelSkuId(channelListingForm.getChannelSkuId());
        channelListingPojo.setGlobalSkuId(getGlobalSkuId(clientId, channelListingForm.getClientSkuId()));
        return channelListingPojo;
    }

    @Transactional(readOnly = true)
    private Long getGlobalSkuId(Long clientId, String clientSkuId) {
        ProductPojo productPojo = productApi.getByClientIdAndClientSkuId(clientId, clientSkuId);
        return productPojo.getGlobalSkuId();
    }

    public void throwsIfInvalidChannelId(Long channelId) throws ApiException {
        ChannelPojo channelPojo = channelApi.get(channelId);
        if (isNull(channelPojo) ||
                Objects.equals(channelPojo.getId(), channelApi.getInternalChannelInfo().getId())) {
            throw new ApiException("invalid channel id: " + channelId);
        }
    }

    public void throwsIfInvalidClientSkuId(ChannelListingUploadForm channelListingUploadForm) throws ApiException {
        Long row = 1L;
        List<ErrorData> errorDataList = new ArrayList<>();
        Long clientId = channelListingUploadForm.getClientId();
        for (ChannelListingForm channelListingForm : channelListingUploadForm.getChannelListingFormList()) {
            String clientSkuId = channelListingForm.getClientSkuId();
            if (isNull(productApi.getByClientIdAndClientSkuId(clientId, clientSkuId))) {
                errorDataList.add(
                        new ErrorData(row, "invalid clientskuid: " + clientSkuId)
                );
            }
            row++;
        }
        ValidationUtil.throwsIfNotEmpty(errorDataList);
    }

    public void throwsIfInvalidChannelSkuId(ChannelListingUploadForm channelListingUploadForm) throws ApiException {
        Long clientId = channelListingUploadForm.getClientId();
        Long channelId = channelListingUploadForm.getChannelId();
        Long row = 1L;
        List<ErrorData> errorDataList = new ArrayList<>();
        for (ChannelListingForm channelListingForm: channelListingUploadForm.getChannelListingFormList()) {
            ProductPojo productPojo = productApi.getByClientIdAndClientSkuId(
                    clientId,
                    channelListingForm.getClientSkuId()
            );
            ChannelListingPojo channelListingPojo = channelListingApi.getByChannelIdAndGlobalSkuId(
                    channelId, productPojo.getGlobalSkuId()
            );
            if (!isNull(channelListingPojo)) {
                errorDataList.add(new ErrorData(row, String.format("for clientid: %d, channelid: %d, channelskuid: %s already mapped to this product", clientId, channelId, channelListingPojo.getChannelSkuId())));
            }
            row++;
        }
        ValidationUtil.throwsIfNotEmpty(errorDataList);
    }
}
