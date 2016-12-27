package com.controlcenter.homerestipa.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by david on 14/11/16.
 */
public class LoginStaffJson {

    @JsonProperty("userId")
    private final Integer userId;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("firstLogin")
    private final boolean firstLogin;


    public LoginStaffJson(Integer userId, String name, boolean firstLogin ) {
        this.userId = userId;
        this.name = name;
        this.firstLogin = firstLogin;
    }
}
