package com.departments.dto.data;

import java.util.Date;

/**
 * Created by david on 17/12/16.
 */
public class UploadedStaff {
    private final int depId;
    private final String fullName;
    private final Date dob;
    private final Date startDay;
    private final String position;
    private final String staffEmail;
    private final String comments;
    private final String loginEmail;
    private final String password;

    public UploadedStaff(int depId, String fullName, Date dob, Date startDay, String position, String staffEmail, String comments, String loginEmail, String password) {
        this.depId = depId;
        this.fullName = fullName;
        this.dob = dob;
        this.startDay = startDay;
        this.position = position;
        this.staffEmail = staffEmail;
        this.comments = comments;
        this.loginEmail = loginEmail;
        this.password = password;
    }

    public int getDepId() {
        return depId;
    }

    public String getFullName() {
        return fullName;
    }

    public Date getDob() {
        return dob;
    }

    public Date getStartDay() {
        return startDay;
    }

    public String getPosition() {
        return position;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public String getComments() {
        return comments;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public String getPassword() {
        return password;
    }
}
