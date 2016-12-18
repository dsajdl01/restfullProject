package com.controlcenter.homerestipa.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by david on 17/12/16.
 */
public class StaffDetailsJson {

    @JsonProperty("fullName")
    private final String fullName;

    @JsonProperty("dob")
    private final String dob;

    @JsonProperty("startDay")
    private final String startDay;

    @JsonProperty("position")
    private final String position;

    @JsonProperty("staffEmail")
    private final String staffEmail;

    @JsonProperty("comment")
    private final String comment;

    @JsonProperty("loginEmail")
    private final String loginEmail;

    @JsonProperty("password")
    private final String password;

    @JsonCreator
    public StaffDetailsJson(@JsonProperty("fullName") String fullName,
                            @JsonProperty("dob") String dob,
                            @JsonProperty("startDay") String startDay,
                            @JsonProperty("position") String position,
                            @JsonProperty("staffEmail") String staffEmail,
                            @JsonProperty("comment") String comment,
                            @JsonProperty("loginEmail") String loginEmail,
                            @JsonProperty("password") String password) {
        this.fullName = fullName;
        this.dob = dob;
        this.startDay = startDay;
        this.position = position;
        this.staffEmail = staffEmail;
        this.comment = comment;
        this.loginEmail = loginEmail;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDob() {
        return dob;
    }

    public String getStartDay() {
        return startDay;
    }

    public String getPosition() {
        return position;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public String getComment() {
        return comment;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public String getPassword() {
        return password;
    }
}
