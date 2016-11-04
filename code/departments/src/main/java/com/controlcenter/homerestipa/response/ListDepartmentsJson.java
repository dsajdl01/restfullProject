package com.controlcenter.homerestipa.response;

/**
 * Created by david on 10/07/16.
 */

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListDepartmentsJson {

    @JsonProperty("department")
    private final List<DepartmentJson> department;

    public ListDepartmentsJson(final List<DepartmentJson> department){
        this.department = department;
    }
}
