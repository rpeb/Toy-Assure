package com.increff.assure.dto;

import com.increff.assure.api.*;
import com.increff.assure.dto.helper.OrderDtoHelper;
import com.increff.assure.model.data.ErrorData;
import com.increff.assure.model.data.InvoiceData;
import com.increff.assure.model.data.OrderItemData;
import com.increff.assure.model.form.InternalOrderForm;
import com.increff.assure.model.form.InternalOrderItemForm;
import com.increff.assure.pojo.OrderPojo;
import com.increff.assure.pojo.ProductPojo;
import com.increff.assure.util.InvoiceUtil;
import com.increff.assure.util.ValidationUtil;
import com.increff.commons.exception.ApiException;
import com.increff.commons.model.form.ChannelOrderForm;
import com.increff.commons.model.form.ChannelOrderItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderDto {

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private OrderItemDto orderItemDto;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private ChannelApi channelApi;

    @Autowired
    private OrderItemApi orderItemApi;

//    public void createOrder(OrderForm orderForm) throws ApiException {
//
//        validate(orderForm.getOrderCSVItemList());
//        userApi.throwsIfInvalidClientId(orderForm.getClientId());
//        userApi.throwsIfInvalidCustomerId(orderForm.getCustomerId());
//        channelApi.throwsIfInvalidChannelId(orderForm.getChannelId(), internalOrder);
//        throwsIfInvalidClientSkuId(orderForm.getClientId(), orderForm.getOrderCSVItemList());
//        OrderPojo insertedOrderPojo = orderApi.createOrder(convertOrderFormToOrderPojo(orderForm, internalOrder));
//        orderItemDto.createOrderItems(orderForm.getOrderCSVItemList(), insertedOrderPojo, internalOrder);
//    }

    public void createInternalOrder(InternalOrderForm orderForm) throws ApiException {
        OrderDtoHelper.validateInternalOrder(orderForm);
        OrderDtoHelper.validateInternalOrderItems(orderForm.getInternalOrderItems());
        OrderDtoHelper.throwsIfDuplicateClientSkus(orderForm.getInternalOrderItems());
        userApi.throwsIfInvalidClientId(orderForm.getClientId());
        userApi.throwsIfInvalidCustomerId(orderForm.getCustomerId());
        List<String> clientSkuIdsList = orderForm.getInternalOrderItems()
                .stream()
                .map(InternalOrderItemForm::getClientSkuId)
                .collect(Collectors.toList());
        throwsIfInvalidClientSkuId(orderForm.getClientId(), clientSkuIdsList);
        OrderPojo insertedOrderPojo = orderApi.createOrder(OrderDtoHelper.convertInternalOrderFormToOrderPojo(orderForm));
        orderItemDto.createInternalOrderItems(orderForm.getInternalOrderItems(), insertedOrderPojo);
    }

    public void createChannelOrder(ChannelOrderForm orderForm) throws ApiException {
        OrderDtoHelper.validateChannelOrder(orderForm);
        OrderDtoHelper.validateChannelOrderItems(orderForm.getChannelOrderItems());
        OrderDtoHelper.throwsIfDuplicateChannelSkus(orderForm.getChannelOrderItems());
        userApi.throwsIfInvalidClientId(orderForm.getClientId());
        userApi.throwsIfInvalidCustomerId(orderForm.getCustomerId());
        List<String> clientSkuIds = orderForm.getChannelOrderItems()
                .stream()
                .map(ChannelOrderItemForm::getChannelSkuId)
                .collect(Collectors.toList());
        throwsIfInvalidClientSkuId(orderForm.getClientId(), clientSkuIds);
        OrderPojo insertedOrderPojo = orderApi.createOrder(OrderDtoHelper.convertChannelOrderFormToOrderPojo(orderForm));
        orderItemDto.createChannelOrderItems(orderForm.getChannelOrderItems(), insertedOrderPojo);
    }

    private void throwsIfInvalidClientSkuId(Long clientId, List<String> clientSkuList) throws ApiException {
        Long row = 1L;
        List<ErrorData> errorDataList = new ArrayList<>();
        for (String clientSkuId : clientSkuList) {
            ProductPojo productPojo = productApi.getByClientIdAndClientSkuId(clientId, clientSkuId);
            if (productPojo == null) {
                errorDataList.add(new ErrorData(
                        row,
                        "invalid clientSkuId: " + clientSkuId)
                );
            }
            row++;
        }
        ValidationUtil.throwsIfNotEmpty(errorDataList);
    }

    public String getInvoice(Long orderId) throws ApiException, IOException, TransformerException {
        OrderPojo orderPojo = orderApi.getOrderById(orderId);
        if (isNull(orderPojo)) {
            throw new ApiException("invalid orderid: " + orderId);
        }
        if (!isNull(orderPojo.getInvoiceUrl())) {
            return orderPojo.getInvoiceUrl();
        }
        String url = createPdfAndGetUrl(orderId);
        orderApi.setInvoiceUrl(orderId, url);
        return url;
    }

    private String createPdfAndGetUrl(Long orderId) throws ApiException, IOException, TransformerException {
        OrderPojo orderPojo = orderApi.getOrderById(orderId);
        if (isNull(orderPojo)) {
            throw new ApiException("invalid orderid: " + orderId);
        }
        List<OrderItemData> orderItemDataList = orderItemApi.getOrderItemsByOrderId(orderId);

        ZonedDateTime time = orderPojo.getCreatedAt();
        double total = 0.0;
        for (OrderItemData orderItemData : orderItemDataList) {
            total += orderItemData.getOrderedQuantity() * orderItemData.getSellingPricePerUnit();
        }

        Long clientId = orderApi.getOrderById(orderId).getClientId();

        String clientName = userApi.getUserById(clientId).getName();

        InvoiceData invoiceData = new InvoiceData(time, orderId, clientName, orderItemDataList, total);

        String xml = InvoiceUtil.jaxbObjectToXML(invoiceData);
        File xsltFile = new File("src", "invoice.xml");
        File pdfFile = new File("src", "invoice.pdf");
        System.out.println(xml);
        InvoiceUtil.convertToPDF(invoiceData, xsltFile, pdfFile, xml);
        return null;
    }


}
