package com.controlcenter.homerestipa.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by david on 27/12/16.
 */
public class StaffLoginDetailsJson extends StaffDetailsJson {

    @JsonProperty("loginEmail")
    private final String loginEmail;

    @JsonProperty("password")
    private final String password;

    public StaffLoginDetailsJson(@JsonProperty("fullName") String fullName,
                                 @JsonProperty("dob") String dob,
                                 @JsonProperty("startDay") String startDay,
                                 @JsonProperty("position") String position,
                                 @JsonProperty("staffEmail") String staffEmail,
                                 @JsonProperty("comment") String comment,
                                 @JsonProperty("loginEmail") String loginEmail,
                                 @JsonProperty("password") String password) {

        super(fullName, dob, startDay, position, staffEmail, comment);
        this.loginEmail = loginEmail;
        this.password = password;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public String getPassword() {
        return password;
    }
}
