package com.increff.assure.dto;

import com.increff.assure.model.form.ProductForm;
import com.increff.assure.model.form.UserForm;
import com.increff.assure.pojo.UserType;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QAConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class ProductDtoTest {
    @Resource
    private ProductDto productDto;

    @Resource
    private UserDto userDto;

    @Test
    public void bulkUploadTest() throws ApiException {
        Long clientId = getClientId();
        List<ProductForm> productFormList = new ArrayList<>();
        int numberOfProducts = 5;
        for (int i = 0; i < numberOfProducts; ++i) {
            productFormList.add(getProductForm());
        }
        productDto.bulkUpload(productFormList, clientId);
        assertEquals(productDto.getAll().size(), numberOfProducts);
    }

    @Test
    public void bulkUploadFailsForInvalidClientId() throws ApiException {
        Long clientId = getCustomerId();
        List<ProductForm> productFormList = new ArrayList<>();
        int numberOfProducts = 5;
        for (int i = 0; i < numberOfProducts; ++i) {
            productFormList.add(getProductForm());
        }
        try {
            productDto.bulkUpload(productFormList, clientId);
        } catch (ApiException e) {
            assertEquals("invalid clientId: " + clientId, e.getMessage());
        }
    }

    private Long getCustomerId() throws ApiException {
        String name = RandomUtil.getRandomString();
        UserForm userForm = new UserForm();
        userForm.setName(name);
        userForm.setType(UserType.CUSTOMER);
        userDto.addUser(userForm);
        return userDto.getAllUsers().get(0).getId();
    }

    private ProductForm getProductForm() {
        ProductForm productForm = new ProductForm();
        productForm.setName(RandomUtil.getRandomString());
        productForm.setDescription(RandomUtil.getRandomString(40));
        productForm.setMrp(Math.random()*100);
        productForm.setBrandId(RandomUtil.getRandomString());
        productForm.setClientSkuId(RandomUtil.getRandomString());
        return productForm;
    }

    private Long getClientId() throws ApiException {
        String name = RandomUtil.getRandomString();
        UserForm userForm = new UserForm();
        userForm.setName(name);
        userForm.setType(UserType.CLIENT);
        userDto.addUser(userForm);
        return userDto.getAllUsers().get(0).getId();
    }

}