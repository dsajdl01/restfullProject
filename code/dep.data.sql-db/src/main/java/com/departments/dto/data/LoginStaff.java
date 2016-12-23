package com.departments.dto.data;

/**
 * Created by david on 16/11/16.
 */
public class LoginStaff {

    private Integer userId;

    private String  name;

    public LoginStaff(Integer userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}
