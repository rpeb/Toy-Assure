package com.increff.assure.dto.helper;


import com.increff.assure.api.*;
import com.increff.assure.model.data.ErrorData;
import com.increff.assure.model.form.InternalOrderForm;
import com.increff.assure.model.form.InternalOrderItemForm;
import com.increff.assure.pojo.*;
import com.increff.assure.util.InvoiceUploadException;
import com.increff.assure.util.S3Service;
import com.increff.assure.util.ValidationUtil;
import com.increff.commons.exception.ApiException;
import com.increff.commons.model.data.ChannelInvoiceForm;
import com.increff.commons.model.data.InvoiceData;
import com.increff.commons.model.data.OrderItemData;
import com.increff.commons.model.form.ChannelOrderForm;
import com.increff.commons.model.form.ChannelOrderItemForm;
import com.increff.commons.util.InvoiceUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderDtoHelper {

    private static final RestTemplate restTemplate = new RestTemplate();
    @Value("${channel.baseUrl}")
    private String channelBaseUrl;

    @Value("${invoice-bucket}")
    private String invoiceBucket;

    @Autowired
    private ChannelApi channelApi;
    @Autowired
    private ProductApi productApi;
    @Autowired
    private OrderApi orderApi;
    @Autowired
    private UserApi userApi;
    @Autowired
    private InventoryApi inventoryApi;
    @Autowired
    private OrderItemApi orderItemApi;
    @Autowired
    private S3Service s3Service;

    public static void throwsIfDuplicateClientSkus(List<InternalOrderItemForm> internalOrderItems) throws ApiException {
        Set<String> clientSkuIds = new HashSet<>();
        Long row = 1L;
        for (InternalOrderItemForm itemForm : internalOrderItems) {
            if (clientSkuIds.contains(itemForm.getClientSkuId())) {
                throw new ApiException("row: " + row + " duplicate clientskuid: " + itemForm.getClientSkuId() + " found");
            } else {
                clientSkuIds.add(itemForm.getClientSkuId());
            }
            row++;
        }
    }

    public static void validateInternalOrderItems(List<InternalOrderItemForm> internalOrderItems) throws ApiException {
        if (CollectionUtils.isEmpty(internalOrderItems)) {
            throw new ApiException("order items list is empty");
        }
        Long row = 1L;
        for (InternalOrderItemForm itemForm : internalOrderItems) {
            if (isNull(itemForm.getClientSkuId()) || itemForm.getClientSkuId().isEmpty()) {
                throw new ApiException("row: " + row + " clientSkuid is empty");
            }
            if (isNull(itemForm.getOrderedQuantity())) {
                throw new ApiException("row: " + row + " ordered quantity is empty");
            }
            if (itemForm.getOrderedQuantity() < 0) {
                throw new ApiException("row: " + row + " ordered quantity is negative");
            }
            if (isNull(itemForm.getSellingPricePerUnit())) {
                throw new ApiException("row: " + row + " selling price per unit is negative");
            }
            if (itemForm.getSellingPricePerUnit() < 0) {
                throw new ApiException("row: " + row + " selling price per unit is negative");
            }
            row++;
        }
    }

    public static void validateInternalOrder(InternalOrderForm orderForm) throws ApiException {
        if (isNull(orderForm)) {
            throw new ApiException("order form is empty");
        }
        if (isNull(orderForm.getChannelOrderId()) || orderForm.getChannelOrderId().isEmpty()) {
            throw new ApiException("channel order id not found");
        }
        if (isNull(orderForm.getClientId())) {
            throw new ApiException("client id not found");
        }
        if (isNull(orderForm.getCustomerId())) {
            throw new ApiException("customer id not found");
        }
        if (isNull(orderForm.getInternalOrderItems())) {
            throw new ApiException("order csv not found");
        }
    }

    public static void validateChannelOrder(ChannelOrderForm orderForm) throws ApiException {
        if (isNull(orderForm)) {
            throw new ApiException("order form is empty");
        }
        if (isNull(orderForm.getChannelOrderId()) || orderForm.getChannelOrderId().isEmpty()) {
            throw new ApiException("channel order id not found");
        }
        if (isNull(orderForm.getClientId())) {
            throw new ApiException("client id not found");
        }
        if (isNull(orderForm.getCustomerId())) {
            throw new ApiException("customer id not found");
        }
        if (isNull(orderForm.getChannelId())) {
            throw new ApiException("channel id not found");
        }
        if (isNull(orderForm.getChannelOrderItems())) {
            throw new ApiException("order csv not found");
        }
    }

    public static void validateChannelOrderItems(List<ChannelOrderItemForm> channelOrderItems) throws ApiException {
        if (CollectionUtils.isEmpty(channelOrderItems)) {
            throw new ApiException("order items list is empty");
        }
        Long row = 1L;
        for (ChannelOrderItemForm itemForm : channelOrderItems) {
            if (isNull(itemForm.getChannelSkuId()) || itemForm.getChannelSkuId().isEmpty()) {
                throw new ApiException("row: " + row + " channel sku id is empty");
            }
            if (isNull(itemForm.getOrderedQuantity())) {
                throw new ApiException("row: " + row + " ordered quantity is empty");
            }
            if (itemForm.getOrderedQuantity() < 0) {
                throw new ApiException("row: " + row + " ordered quantity is negative");
            }
            if (isNull(itemForm.getSellingPricePerUnit())) {
                throw new ApiException("row: " + row + " selling price per unit is negative");
            }
            if (itemForm.getSellingPricePerUnit() < 0) {
                throw new ApiException("row: " + row + " selling price per unit is negative");
            }
            row++;
        }
    }

    public static void throwsIfDuplicateChannelSkus(List<ChannelOrderItemForm> channelOrderItems) throws ApiException {
        Set<String> clientSkuIds = new HashSet<>();
        Long row = 1L;
        for (ChannelOrderItemForm itemForm : channelOrderItems) {
            if (clientSkuIds.contains(itemForm.getChannelSkuId())) {
                throw new ApiException("row: " + row + " duplicate clientskuid: " + itemForm.getChannelSkuId() + " found");
            } else {
                clientSkuIds.add(itemForm.getChannelSkuId());
            }
            row++;
        }
    }

    public static OrderPojo convertChannelOrderFormToOrderPojo(ChannelOrderForm orderForm) {
        OrderPojo pojo = new OrderPojo();
        pojo.setCustomerId(orderForm.getCustomerId());
        pojo.setChannelId(orderForm.getChannelId());
        pojo.setStatus(OrderStatus.CREATED);
        pojo.setChannelOrderId(orderForm.getChannelOrderId());
        pojo.setClientId(orderForm.getClientId());
        return pojo;
    }

    public void throwsIfInvalidClientSkuId(Long clientId, List<String> clientSkuList) throws ApiException {
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

    public OrderPojo convertInternalOrderFormToOrderPojo(InternalOrderForm orderForm) {
        OrderPojo pojo = new OrderPojo();
        pojo.setChannelId(channelApi.getInternalChannelInfo().getId());
        pojo.setChannelOrderId(orderForm.getChannelOrderId());
        pojo.setStatus(OrderStatus.CREATED);
        pojo.setClientId(orderForm.getClientId());
        pojo.setCustomerId(orderForm.getCustomerId());
        return pojo;
    }

    public void throwsIfInternalChannelIdForChannelOrder(Long channelId) throws ApiException {
        if (channelApi.getInternalChannelInfo().getId().equals(channelId)) {
            throw new ApiException("invalid channel id: " + channelId + " for channel order");
        }
    }

    public void throwsIfOrderStatusNotCreated(Long orderId) throws ApiException {
        OrderPojo pojo = orderApi.getOrderById(orderId);
        if (isNull(pojo)) {
            throw new ApiException("order does not exist");
        }
        if (!pojo.getStatus().equals(OrderStatus.CREATED)) {
            throw new ApiException("cannot allocate this order");
        }
    }

    public String getInvoice(Long orderId) throws InvoiceUploadException, ApiException, IOException, TransformerException {
        OrderPojo orderPojo = orderApi.getOrderById(orderId);
        if (isNull(orderPojo)) {
            throw new ApiException("invalid orderid: " + orderId);
        }
        if (!isNull(orderPojo.getInvoiceUrl())) {
            return orderPojo.getInvoiceUrl();
        }
        if (!orderPojo.getStatus().equals(OrderStatus.FULFILLED)) {
            throw new ApiException("cannot fetch invoice: order not fulfilled");
        }
        ChannelPojo channel = channelApi.get(orderPojo.getChannelId());
        String url = null;
        if (channel.getInvoiceType().equals(InvoiceType.SELF)) {
            url = createPdfAndGetUrl(orderPojo);
        } else {
            getInvoiceFromChannelModule(orderPojo);
            url = uploadChannelInvoice(orderId.toString());
        }
        orderPojo.setInvoiceUrl(url);
        return url;
    }

    private String uploadChannelInvoice(String key) throws InvoiceUploadException {
        s3Service.setup(invoiceBucket);
        File file = new File("src/channel-invoice.pdf");
        return s3Service.upload(invoiceBucket, file, key);
    }

    private void getInvoiceFromChannelModule(OrderPojo orderPojo) throws ApiException {
        ChannelInvoiceForm channelInvoiceForm = new ChannelInvoiceForm();
        List<OrderItemData> orderItemDataList = orderItemApi.getOrderItemDataListByOrderId(orderPojo.getId());
        ZonedDateTime time = orderPojo.getCreatedAt();
        double total = 0.0;
        for (OrderItemData orderItemData : orderItemDataList) {
            total += orderItemData.getOrderedQuantity() * orderItemData.getSellingPricePerUnit();
        }
        String clientName = userApi.getUserById(orderPojo.getClientId()).getName();
        InvoiceData invoiceData = new InvoiceData(time, orderPojo.getId(), clientName, orderItemDataList, total);
        channelInvoiceForm.setInvoiceData(invoiceData);
        String url = null;
        try {
            String uri = channelBaseUrl + "/order/invoice";
            byte[] bytes = restTemplate.postForObject(uri, channelInvoiceForm, byte[].class);
            FileUtils.writeByteArrayToFile(new File("src/channel-invoice.pdf"), bytes);
        } catch (RestClientException e) {
            throw new ApiException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String createPdfAndGetUrl(OrderPojo orderPojo) throws InvoiceUploadException, ApiException, IOException, TransformerException {
        List<OrderItemData> orderItemDataList = orderItemApi.getOrderItemDataListByOrderId(orderPojo.getId());

        ZonedDateTime time = orderPojo.getCreatedAt();
        double total = 0.0;
        for (OrderItemData orderItemData : orderItemDataList) {
            total += orderItemData.getOrderedQuantity() * orderItemData.getSellingPricePerUnit();
        }

        String clientName = userApi.getUserById(orderPojo.getClientId()).getName();

        InvoiceData invoiceData = new InvoiceData(time, orderPojo.getId(), clientName, orderItemDataList, total);

        String xml = InvoiceUtil.jaxbObjectToXML(invoiceData);
        File xsltFile = new File("src", "invoice.xml");
        File pdfFile = new File("src", "invoice.pdf");
        System.out.println(xml);
        InvoiceUtil.convertToPDF(xsltFile, pdfFile, xml);
        s3Service.setup(invoiceBucket);
        return s3Service.upload(invoiceBucket, pdfFile, orderPojo.getId().toString());
    }

    public Map<Long, Long> getMapOfInventoryQuantities(List<OrderItemPojo> orderItems) {
        Map<Long, Long> inventoryQuantityMap = new HashMap<>();
        for (OrderItemPojo orderItem : orderItems) {
            InventoryPojo inventoryPojo = inventoryApi.getByGlobalSkuId(orderItem.getGlobalSkuId());
            inventoryQuantityMap.put(orderItem.getGlobalSkuId(), inventoryPojo.getAvailableQuantity());
        }
        return inventoryQuantityMap;
    }

    public void throwsIfOrderStatusNotAllocated(Long orderId) throws ApiException {
        OrderPojo pojo = orderApi.getOrderById(orderId);
        if (isNull(pojo)) {
            throw new ApiException("invalid order id: " + orderId);
        }
        if (!pojo.getStatus().equals(OrderStatus.ALLOCATED)) {
            throw new ApiException("cannot fulfill this order: not allocated yet");
        }
    }
}
