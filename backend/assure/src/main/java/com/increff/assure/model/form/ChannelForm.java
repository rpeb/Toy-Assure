package com.increff.assure.model.form;

import com.increff.assure.pojo.InvoiceType;
import lombok.Data;

@Data
public class ChannelForm {
    private String name;
    private InvoiceType invoiceType;
}
