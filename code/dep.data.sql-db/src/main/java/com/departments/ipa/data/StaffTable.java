package com.departments.ipa.data;

import java.util.Date;

/**
 * Created by david on 26/08/16.
 */
public class StaffTable {
    private final Integer id;

    private final Integer depId;

    private final String name;

    private final Date dob;

    private final Date startDay;

    private final Date lastDay;

    private final String position;

    private final String email;

    private final String comment;

    public StaffTable(Integer id, Integer depId, String name, Date dob, Date startDay, Date lastDay, String position, String email, String comment) {
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

    public Integer getId() {
        return id;
    }

    public Integer getDepId() {
        return depId;
    }

    public String getName() {
        return name;
    }

    public Date getDob() {
        return dob;
    }

    public Date getStartDay() {
        return startDay;
    }

    public Date getLastDay() {
        return lastDay;
    }

    public String getPosition() {
        return position;
    }

    public String getEmail() {
        return email;
    }

    public String getComment() {
        return comment;
    }
}
