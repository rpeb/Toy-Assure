package com.increff.assure.model.form;

import lombok.Data;

import java.util.List;

@Data
public class ChannelListingUploadForm {

    Long clientId;

    Long channelId;

    List<ChannelListingForm> channelListingFormList;
}
