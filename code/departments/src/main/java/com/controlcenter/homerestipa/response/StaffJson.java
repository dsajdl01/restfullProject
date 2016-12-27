package com.controlcenter.homerestipa.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by david on 27/12/16.
 */
public class StaffJson extends StaffDetailsJson {

    @JsonProperty("staffId")
    private int staffId;

    @JsonProperty("depId")
    private int depId;

    @JsonProperty("lastDay")
    private String lastDay;

    public StaffJson(@JsonProperty("staffId") int staffId,
                     @JsonProperty("depId") int depId,
                     @JsonProperty("fullName") String fullName,
                     @JsonProperty("dob") String dob,
                     @JsonProperty("startDay") String startDay,
                     @JsonProperty("lastDay") String lastDay,
                     @JsonProperty("position") String position,
                     @JsonProperty("staffEmail") String staffEmail,
                     @JsonProperty("comment") String comment) {

        super(fullName, dob, startDay, position, staffEmail, comment);
        this.depId = depId;
        this.staffId = staffId;
        this.lastDay = lastDay;
    }
}
