package com.increff.assure.dto;

import com.increff.assure.api.ChannelApi;
import com.increff.assure.api.ChannelListingApi;
import com.increff.assure.api.ProductApi;
import com.increff.assure.api.UserApi;
import com.increff.assure.dto.helper.ChannelListingDtoHelper;
import com.increff.assure.model.form.ChannelListingUploadForm;
import com.increff.assure.pojo.ChannelListingPojo;
import com.increff.commons.exception.ApiException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
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

    @Autowired
    private ChannelListingDtoHelper channelListingDtoHelper;

    @Transactional(rollbackFor = ApiException.class)
    public void add(ChannelListingUploadForm channelListingUploadForm) throws ApiException {
        ChannelListingDtoHelper.validateAndNormalizeList(channelListingUploadForm);
        channelListingDtoHelper.throwsIfInvalidChannelId(channelListingUploadForm.getChannelId());
        channelListingDtoHelper.throwsIfInvalidChannelSkuId(channelListingUploadForm);
        userApi.throwsIfInvalidClientId(channelListingUploadForm.getClientId());
        channelListingDtoHelper.throwsIfInvalidClientSkuId(channelListingUploadForm);
        List<ChannelListingPojo> channelListingPojos = channelListingDtoHelper.convertChannelListingUploadFormToChannelListingPojoList(channelListingUploadForm);
        channelListingApi.add(channelListingPojos);
    }
}
