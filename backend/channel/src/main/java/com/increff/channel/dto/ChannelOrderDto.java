package com.increff.channel.dto;

import com.increff.commons.exception.ApiException;
import com.increff.commons.model.data.ChannelInvoiceForm;
import com.increff.commons.model.data.InvoiceData;
import com.increff.commons.model.form.ChannelOrderForm;
import com.increff.commons.util.InvoiceUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ChannelOrderDto {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${assure.baseUrl}")
    private String assureBaseUrl;

    public void createChannelOrder(ChannelOrderForm orderForm) throws ApiException {
        try {
            restTemplate.postForObject(assureBaseUrl + "/order/channel", orderForm, Void.class);
        } catch (RestClientException e) {
            throw new ApiException(e.getMessage());
        }
    }

    public byte[] getInvoice(ChannelInvoiceForm channelInvoiceForm) throws IOException, TransformerException {
        InvoiceData invoiceData = channelInvoiceForm.getInvoiceData();
        String xml = InvoiceUtil.jaxbObjectToXML(invoiceData);
        File xsltFile = new File("src", "invoice.xml");
        File pdfFile = new File("src", "invoice.pdf");
        System.out.println(xml);
        InvoiceUtil.convertToPDF(xsltFile, pdfFile, xml);
        return Files.readAllBytes(Paths.get("src/invoice.pdf"));
    }
}
