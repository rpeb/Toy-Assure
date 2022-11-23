package com.increff.assure.api;

import com.increff.assure.dao.ChannelListingDao;
import com.increff.assure.model.data.ErrorData;
import com.increff.assure.pojo.ChannelListingPojo;
import com.increff.assure.util.ValidationUtil;
import com.increff.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ChannelListingApi {
    @Autowired
    private ChannelListingDao channelListingDao;

    public void add(List<ChannelListingPojo> channelListingPojos) throws ApiException {
        List<ErrorData> errorDataList = new ArrayList<>();
        Long row = 1L;
        for (ChannelListingPojo channelListingPojo : channelListingPojos) {
            ChannelListingPojo pojo = channelListingDao.selectByClientIdChannelIdChannelSkuId(
                    channelListingPojo.getClientId(),
                    channelListingPojo.getChannelId(),
                    channelListingPojo.getChannelSkuId()
            );
            if (pojo != null) {
                errorDataList.add(
                        new ErrorData(row, "channelSkuId, channelId, clientId must be unique")
                );
            }
            row++;
        }
        ValidationUtil.throwsIfNotEmpty(errorDataList);
        channelListingPojos.forEach(channelListingPojo -> channelListingDao.insert(channelListingPojo));
    }

    @Transactional(readOnly = true)
    public ChannelListingPojo getByClientIdChannelIdChannelSkuId(
            Long clientId, Long channelId, String channelSkuId
    ) {
        return channelListingDao.selectByClientIdChannelIdChannelSkuId(clientId, channelId, channelSkuId);
    }

    @Transactional(readOnly = true)
    public ChannelListingPojo getByChannelIdAndGlobalSkuId(Long channelId, Long globalSkuId) {
        return channelListingDao.selectByChannelIdAndGlobalSkuId(channelId, globalSkuId);
    }
}
