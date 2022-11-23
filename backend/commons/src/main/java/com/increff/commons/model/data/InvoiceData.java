package com.increff.commons.model.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement
public class InvoiceData {
    private Long orderId;
    private String clientName;
    private String invoiceGenerationTime;
    private List<OrderItemData> orderItemDataList;
    private Double total;

    public InvoiceData(ZonedDateTime invoiceGenerationTime, Long orderId, String clientName, List<OrderItemData> orderItemDataList, Double total) {
        this.invoiceGenerationTime = invoiceGenerationTime.toLocalDate().toString() + " " + invoiceGenerationTime.toLocalTime().toString();
        this.orderId = orderId;
        this.clientName = clientName;
        this.orderItemDataList = orderItemDataList;
        this.total = total;
    }
}
