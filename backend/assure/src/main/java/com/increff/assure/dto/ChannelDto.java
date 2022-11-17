package com.increff.assure.dto;

import com.increff.assure.api.ChannelApi;
import com.increff.assure.dto.helper.ChannelDtoHelper;
import com.increff.assure.model.form.ChannelForm;
import com.increff.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChannelDto {

    @Autowired
    private ChannelApi service;

    @Transactional
    public void add(ChannelForm channelForm) throws ApiException {
        ChannelDtoHelper.normalize(channelForm);
        service.add(ChannelDtoHelper.convertChannelFormToChannelPojo(channelForm));
    }
}
