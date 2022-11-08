package com.increff.assure.dto.helper;

import com.increff.assure.exception.ApiException;
import com.increff.assure.model.ChannelForm;
import com.increff.assure.pojo.ChannelPojo;

public class ChannelDtoHelper {

    public static void normalize(ChannelForm channelForm) throws ApiException {
        validate(channelForm);
        channelForm.setName(channelForm.getName().trim().toLowerCase());
    }

    private static void validate(ChannelForm channelForm) throws ApiException {
        if (channelForm == null) {
            throw new ApiException("channel details cannot be empty");
        }
    }

    public static ChannelPojo convertChannelFormToChannelPojo(ChannelForm channelForm) {
        ChannelPojo channelPojo = new ChannelPojo();
        channelPojo.setName(channelForm.getName());
        channelPojo.setInvoiceType(channelForm.getInvoiceType());
        return channelPojo;
    }
}