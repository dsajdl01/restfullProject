package com.controlcenter.homerestipa.utils;


import com.controlcenter.homerestipa.response.StaffDetailsJson;
import com.departments.dto.data.LoginDetails;
import com.departments.dto.data.StaffTable;

import java.util.Date;

/**
 * Created by david on 21/12/16.
 */
public class DataMapper {


    public StaffTable mapStaffTable(int depId, StaffDetailsJson staff, Date dob, Date startDay) {
        return new StaffTable(null, depId, staff.getFullName(), dob, startDay,null, staff.getPosition(), staff.getLoginEmail(), staff.getComment());
    }

    public LoginDetails mapLoginDetails(String email, String password) {
        return new LoginDetails(email, password);
    }

}