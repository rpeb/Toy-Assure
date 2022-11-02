package com.increff.assure.model;

import com.increff.assure.pojo.UserType;
import lombok.Data;

@Data
public class UserForm {
    private String name;
    private UserType type;
}
