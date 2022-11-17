package com.increff.assure.model.form;

import com.increff.commons.model.form.OrderCSVItemForm;
import lombok.Data;

import java.util.List;

@Data
public class InternalOrderForm {
    private Long clientId;
    private Long customerId;
    private String channelOrderId;
    private List<InternalOrderItemForm> internalOrderItems;
}
