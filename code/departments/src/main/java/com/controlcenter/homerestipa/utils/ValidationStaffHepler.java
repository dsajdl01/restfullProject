package com.controlcenter.homerestipa.utils;

import com.controlcenter.homerestipa.response.StaffDetailsJson;
import com.departments.ipa.common.lgb.CommonConversions;
import com.departments.ipa.data.LoginDetails;
import com.departments.ipa.data.StaffTable;
import com.departments.ipa.fault.exception.ValidationException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by david on 17/12/16.
 */
public class ValidationStaffHepler{

    private CommonConversions commonConv = new CommonConversions();

    public ValidationStaffHepler() {}

    public void basicValidateDepartmentId(Integer depId) throws ValidationException {
        if (depId == null ) {
            throw new ValidationException("Mandatory department ID is missing");
        } else if ( depId <= 0 ) {
            throw new ValidationException("Invalid department ID");
        }
    }

    public void  basicValidateStaffObject(StaffDetailsJson staff) throws ValidationException {
        if ( staff == null ) {
            throw new ValidationException("Mandatory staff object is missing");
        }
    }

    public LoginDetails validateLoginDetails(String email, String password) throws ValidationException {
        String message = "";
        if ( commonConv.stringIsNullOrEmpty(email) ) {
            message = "Mandatory login email is missing. ";
        }

        if ( commonConv.stringIsNullOrEmpty(password) ) {
            message += "Mandatory password is missing. ";
        }

        if ( ! commonConv.emailValidation(email) ) {
            message += "Invalid email";
        }

        if ( !message.equals("") ) {
            throw new ValidationException( message );
        }

        return new LoginDetails(email, password);
    }

    public StaffTable getStaffTable(int depId, StaffDetailsJson staff) throws  ValidationException {

        List<String> valResponseList = validateStaffDetails(staff);
        Date dob = convertDates(staff.getDob(), "", valResponseList);
        Date startDay = convertDates(staff.getStartDay(), "", valResponseList);;

        String errorMsg = "";
        if ( !valResponseList.isEmpty() ) {
            for ( String msg : valResponseList ) {
                errorMsg += msg + " ";
            }
            throw new ValidationException( errorMsg );
        }

        return new StaffTable(null, depId, staff.getFullName(), dob, startDay,null, staff.getPosition(), staff.getLoginEmail(), staff.getComment());
    }

    private Date convertDates(String date, String message, List<String> buildResponseList) {
        try {
            return commonConv.getDateFromString(date);
        } catch ( ParseException e ) {
            buildResponseList.add(message);
        }
        return null;
    }

    private List<String> validateStaffDetails(StaffDetailsJson staff ) {
        List<String> errorMessage = new ArrayList<>();

        if (commonConv.stringIsNullOrEmpty(staff.getFullName())) {
            errorMessage.add("Mandatory name is missing.");
        }

        if (commonConv.stringIsNullOrEmpty(staff.getDob())) {
            errorMessage.add("Mandatory date of birthday is missing.");
        }

        if (commonConv.stringIsNullOrEmpty(staff.getStartDay())) {
            errorMessage.add("Mandatory start day is missing.");
        }

        if (commonConv.stringIsNullOrEmpty(staff.getPosition())) {
            errorMessage.add("Mandatory position is missing.");
        }
        return errorMessage;
    }

}
