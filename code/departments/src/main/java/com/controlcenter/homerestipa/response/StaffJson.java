package com.controlcenter.homerestipa.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by david on 14/11/16.
 */
public class StaffJson {

    @JsonProperty("id")
    private final Integer id;

    @JsonProperty("depId")
    private final Integer depId;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("dob")
    private final Date dob;

    @JsonProperty("startDay")
    private final Date startDay;

    @JsonProperty("lastDay")
    private final Date lastDay;

    @JsonProperty("position")
    private final String position;

    @JsonProperty("email")
    private final String email;

    @JsonProperty("comment")
    private final String comment;

    public StaffJson(Integer id, Integer depId, String name, Date dob, Date startDay, Date lastDay, String position, String email, String comment) {
        this.id = id;
        this.depId = depId;
        this.name = name;
        this.dob = dob;
        this.startDay = startDay;
        this.lastDay = lastDay;
        this.position = position;
        this.email = email;
        this.comment = comment;
    }
}
