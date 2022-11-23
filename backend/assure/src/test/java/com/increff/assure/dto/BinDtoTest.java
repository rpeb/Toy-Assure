package com.increff.assure.dto;

import com.increff.assure.util.RandomUtil;
import com.increff.commons.exception.ApiException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QAConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class BinDtoTest {

    @Resource
    private BinDto binDto;

    @Test
    public void addBin() throws ApiException {
        Long numberOfBins = RandomUtil.getRandomNumberBetween1to100();
        Long numberOfBinsCreated = (long) binDto.addBin(numberOfBins).size();
        assertEquals(numberOfBinsCreated, numberOfBins);
    }

    @Test
    public void binSize() throws ApiException {
        Long numberOfBins = RandomUtil.getRandomNumberBetween1to100();
        binDto.addBin(numberOfBins);
        Long totalNumberOfBins = (long) binDto.getAll().size();
        assertEquals(numberOfBins, totalNumberOfBins);
    }

    @Test
    public void throwsErrorForNegativeNumberOfBins() {
        Long numberOfBins = -1L;
        try {
            binDto.addBin(numberOfBins);
        } catch (ApiException e) {
            assertEquals("number of bins cannot be negative", e.getMessage());
        }
    }
}