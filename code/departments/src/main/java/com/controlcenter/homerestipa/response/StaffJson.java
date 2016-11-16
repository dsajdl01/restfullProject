package com.controlcenter.homerestipa.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by david on 14/11/16.
 */
public class StaffJson {

    @JsonProperty("userId")
    private final Integer userId;

    @JsonProperty("name")
    private final String name;


    public StaffJson( Integer userId, String name ) {
        this.userId = userId;
        this.name = name;
    }
}
