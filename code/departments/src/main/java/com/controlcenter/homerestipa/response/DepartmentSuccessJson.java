package com.controlcenter.homerestipa.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by david on 04/09/16.
 */
public class DepartmentSuccessJson {

    @JsonProperty
    private final Integer code;

    @JsonProperty
    private final String message;

    public DepartmentSuccessJson(final Integer code, final String message) {
        this.code = code;
        this.message = message;
    }
}
