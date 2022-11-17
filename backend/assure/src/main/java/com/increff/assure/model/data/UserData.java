package com.increff.assure.model.data;

import com.increff.assure.pojo.UserType;
import lombok.Data;

@Data
public class UserData {
    private Long id;
    private String name;
    private UserType type;
}
