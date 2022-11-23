package com.increff.assure.dto.helper;

import com.increff.assure.model.form.ChannelForm;
import com.increff.assure.model.form.ChannelNameUpdateForm;
import com.increff.assure.pojo.ChannelPojo;
import com.increff.commons.exception.ApiException;
import com.mysql.cj.util.StringUtils;

import static java.util.Objects.isNull;

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

    public static void normalizeChannelNameUpdateForm(ChannelNameUpdateForm channelNameUpdateForm) throws ApiException {
        if (isNull(channelNameUpdateForm)) {
            throw new ApiException("update details not found");
        }
        if (StringUtils.isNullOrEmpty(channelNameUpdateForm.getOldName())) {
            throw new ApiException("old name field is missing");
        }
        if (StringUtils.isNullOrEmpty(channelNameUpdateForm.getNewName())) {
            throw new ApiException("new name field is empty");
        }
        channelNameUpdateForm.setOldName(channelNameUpdateForm.getOldName().trim().toLowerCase());
        channelNameUpdateForm.setNewName(channelNameUpdateForm.getNewName().trim().toLowerCase());
    }
}
