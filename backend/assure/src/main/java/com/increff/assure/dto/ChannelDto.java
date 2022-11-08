package com.increff.assure.dto;

import com.increff.assure.api.ChannelService;
import com.increff.assure.dto.helper.ChannelDtoHelper;
import com.increff.assure.exception.ApiException;
import com.increff.assure.model.ChannelForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChannelDto {

    @Autowired
    private ChannelService service;

    @Transactional
    public void add(ChannelForm channelForm) throws ApiException {
        ChannelDtoHelper.normalize(channelForm);
        service.add(ChannelDtoHelper.convertChannelFormToChannelPojo(channelForm));
    }
}
