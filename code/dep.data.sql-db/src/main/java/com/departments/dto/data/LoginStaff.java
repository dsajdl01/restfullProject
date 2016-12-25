package com.departments.dto.data;

/**
 * Created by david on 16/11/16.
 */
public class LoginStaff {

    private Integer userId;

    private String  name;

    private boolean firstLogin;

    public LoginStaff(Integer userId, String name, boolean firstLogin) {
        this.userId = userId;
        this.name = name;
        this.firstLogin = firstLogin;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public boolean getFirstLogin() {
        return firstLogin;
    }
}
