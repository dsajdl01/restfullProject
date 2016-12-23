package com.controlcenter.homerestipa.utils;

import com.controlcenter.homerestipa.response.StaffDetailsJson;
import com.department.core.data.PasswordAuthentication;
import com.departments.dto.common.lgb.CommonConversions;
import com.departments.dto.data.LoginDetails;
import com.departments.dto.data.StaffTable;
import com.departments.dto.fault.exception.ValidationException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by david on 17/12/16.
 */
public class ValidationStaffHepler{

    private CommonConversions commonConv = new CommonConversions();
    private PasswordAuthentication passwordAuth;

    private static final int MAX_PASSWORD_LENGTH = 8;

    public ValidationStaffHepler(PasswordAuthentication passwordAuth) {
        this.passwordAuth = passwordAuth;
    }

    public void basicStaffValidation(Integer depId, StaffDetailsJson newStaff) throws ValidationException {
        basicValidateDepartmentId(depId);
        basicValidateStaffObject(newStaff);
    }


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

    public LoginDetails validateAndGetLoginDetails(String email, String password) throws ValidationException {
        String message = "";
        if ( commonConv.stringIsNullOrEmpty(email) ) {
            message = "Mandatory login email is missing. ";
        }

        if ( commonConv.stringIsNullOrEmpty(password) ) {
            message += "Mandatory password is missing. ";
        } else if ( !commonConv.hasStringMaxLength(password, MAX_PASSWORD_LENGTH)) {
             message += "Password must be at least " + MAX_PASSWORD_LENGTH + " characters long. ";
        }

        if ( ! commonConv.emailValidation(email) ) {
            message += "Invalid email";
        }

        if ( !message.equals("") ) {
            throw new ValidationException( message );
        }

        return new DataMapper().mapLoginDetails(email,passwordAuth.hashPassword(password));
    }

    public StaffTable validateAndGetStaffTable(int depId, StaffDetailsJson staff) throws  ValidationException {

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

        return new DataMapper().mapStaffTable(depId, staff, dob, startDay);
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
