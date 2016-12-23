package com.departments.dto.data;

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

    public StaffTable(final Integer id, final Integer depId, final String name, final Date dob, final Date startDay,
                      final Date lastDay, final String position, final String email, final String comment) {
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

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        StaffTable staffTable = (StaffTable) obj;
        return this.id == staffTable.id
                && this.depId == staffTable.depId
                && this.name == staffTable.name
                && this.dob == staffTable.dob
                && this.startDay == staffTable.startDay
                && this.lastDay == staffTable.lastDay
                && this.position == staffTable.position
                && this.email == staffTable.email
                && this.comment == staffTable.comment;
    }

    @Override
    public String toString(){
        return "StaffTable [id = " + this.id + ", depId = " + this.depId + ", name = " + this.name +
                ", dob = " + this.dob + ", start day = " + this.startDay + ", last Day = " + this.lastDay +
                ", position = " + this.position + ", email = " + this.email + ", comment = " + this.comment + "]";
    }
}
