package com.controlcenter.homerestipa.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by david on 28/08/16.
 */
public class DepartmentErrorJson {

    @JsonProperty
    private final Integer errorCode;

    @JsonProperty
    private final String message;

    public DepartmentErrorJson(final Integer errorCode,final String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
