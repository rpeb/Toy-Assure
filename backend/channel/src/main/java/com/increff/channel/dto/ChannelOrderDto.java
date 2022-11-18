package com.increff.channel.dto;

import com.increff.commons.exception.ApiException;
import com.increff.commons.model.form.ChannelOrderForm;
import com.increff.commons.model.form.ChannelOrderItemForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static java.util.Objects.isNull;

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

    public String getInvoice(Long orderId) throws ApiException {
        try {
            String url = assureBaseUrl + "/order/invoice/" + orderId.toString();
            return restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            throw new ApiException(e.getMessage());
        }
    }
}
