package com.controlcenter.homerestipa.response;

/**
 * Created by david on 10/07/16.
 */

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListDepartment {

    @JsonProperty("department")
    private final List<DepartmentJson> department;

    public ListDepartment(final List<DepartmentJson> department){
        this.department = department;
    }
}
