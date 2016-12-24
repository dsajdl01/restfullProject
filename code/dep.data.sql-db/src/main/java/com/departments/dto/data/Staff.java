package com.departments.dto.data;

import java.util.Date;

/**
 * Created by david on 26/08/16.
 */
public final class Staff {

    private final Integer id;

    private final Integer depId;

    private final String name;

    private final Date dob;

    private final Date startDay;

    private final Date lastDay;

    private final String position;

    private final String email;

    private final String comment;

    public Staff(final Integer id, final Integer depId, final String name, final Date dob, final Date startDay,
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
        Staff staff = (Staff) obj;
        return this.id == staff.id
                && this.depId == staff.depId
                && this.name == staff.name
                && this.dob == staff.dob
                && this.startDay == staff.startDay
                && this.lastDay == staff.lastDay
                && this.position == staff.position
                && this.email == staff.email
                && this.comment == staff.comment;
    }

    @Override
    public String toString(){
        return "Staff [id = " + this.id + ", depId = " + this.depId + ", name = " + this.name +
                ", dob = " + this.dob + ", start day = " + this.startDay + ", last Day = " + this.lastDay +
                ", position = " + this.position + ", email = " + this.email + ", comment = " + this.comment + "]";
    }


    private Staff(Builder builder) {

        this.id = builder.id;
        this.depId = builder.depId;
        this.name = builder.name;
        this.dob = builder.dob;
        this.startDay = builder.startDay;
        this.lastDay = builder.lastDay;
        this.position = builder.position;
        this.email = builder.email;
        this.comment = builder.comment;
    }


    public static class Builder {

        private Integer id;
        private Integer depId;
        private String name;
        private Date dob;
        private Date startDay;
        private Date lastDay;
        private String position;
        private String email;
        private String comment;

        public Builder() {
            this.id = null;
            this.depId = 0;
            this.name = null;
            this.dob = null;
            this.startDay = null;
            this.lastDay = null;
            this.position = null;
            this.email = null;
            this.comment = null;
        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setDepId(Integer depId) {
            this.depId = depId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDob(Date dob) {
            this.dob = dob;
            return this;
        }

        public Builder setStartDay(Date startDay) {
            this.startDay = startDay;
            return this;
        }

        public Builder setLastDay(Date lastDay) {
            this.lastDay = lastDay;
            return this;
        }

        public Builder setPosition(String position) {
            this.position = position;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public final Staff build() {
            return new Staff(this);
        }
    }

}
