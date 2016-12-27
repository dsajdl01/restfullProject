package com.controlcenter.homerestipa.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by david on 27/12/16.
 */
public class StaffDetailsListJson {

    @JsonProperty("staffDetailsList")
    public List<StaffJson> staffDetailsList;

    public StaffDetailsListJson(List<StaffJson> staffDetailsList ) {
        this.staffDetailsList = staffDetailsList;
    }
}
