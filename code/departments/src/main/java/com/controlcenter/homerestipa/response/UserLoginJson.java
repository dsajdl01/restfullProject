package com.controlcenter.homerestipa.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by david on 13/11/16.
 */
public class UserLoginJson {

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonCreator
    public UserLoginJson( @JsonProperty("email") final String email,
                          @JsonProperty("password") final String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
