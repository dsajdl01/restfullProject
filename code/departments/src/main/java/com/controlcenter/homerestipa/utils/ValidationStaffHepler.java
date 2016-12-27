package com.controlcenter.homerestipa.utils;

import com.controlcenter.homerestipa.response.StaffLoginDetailsJson;
import com.department.core.data.PasswordAuthentication;
import com.departments.dto.common.lgb.CommonConversions;
import com.departments.dto.data.LoginDetails;
import com.departments.dto.data.Staff;
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
    private static final int MAX_SEARCH_VALUE = 3;

    public ValidationStaffHepler(PasswordAuthentication passwordAuth) {
        this.passwordAuth = passwordAuth;
    }

    public void basicStaffValidation(Integer depId, StaffLoginDetailsJson newStaff) throws ValidationException {
        basicValidationOfDepartmentId(depId);
        basicValidateStaffObject(newStaff);
    }

    public void basicValidationOfSearchValue(String val) throws ValidationException {
        if ( !commonConv.hasStringMaxLength(val, MAX_SEARCH_VALUE) ) {
            throw new ValidationException("Search value mas be at least " + MAX_SEARCH_VALUE + " characters" );
        }
    }

    public void basicValidationOfDepartmentId(Integer depId) throws ValidationException {
        if (depId == null ) {
            throw new ValidationException("Mandatory department ID is missing.");
        } else if ( depId <= 0 ) {
            throw new ValidationException("Invalid department ID.");
        }
    }

    public void  basicValidateStaffObject(StaffLoginDetailsJson staff) throws ValidationException {
        if ( staff == null ) {
            throw new ValidationException("Mandatory staff object is missing.");
        }
    }

    public LoginDetails validateAndGetLoginDetails(String email, String password) throws ValidationException {
        String message = "";
        if ( commonConv.stringIsNullOrEmpty(email) ) {
            message = "Mandatory login email is missing. ";
        } else if ( ! commonConv.doesEmailIsValid(email) ) {
            message += "Invalid email. ";
        }

        if ( commonConv.stringIsNullOrEmpty(password) ) {
            message += "Mandatory password is missing. ";
        } else if ( !commonConv.hasStringMaxLength(password, MAX_PASSWORD_LENGTH)) {
             message += "Password must be at least " + MAX_PASSWORD_LENGTH + " characters long.";
        }

        if ( !message.equals("") ) {
            throw new ValidationException( message );
        }

        return new DataMapper().mapLoginDetails(email,passwordAuth.hashPassword(password));
    }

    public Staff validateAndGetStaffDetails(int depId, StaffLoginDetailsJson staff) throws  ValidationException {

        List<String> valResponseList = validateStaffDetails(staff);
        Date dob = convertDates(staff.getDob(), "Date of birthday", valResponseList);
        Date startDay = convertDates(staff.getStartDay(), "Start day", valResponseList);

        String errorMsg = "";
        if ( !valResponseList.isEmpty() ) {
            for ( String msg : valResponseList ) {
                errorMsg += msg + " ";
            }
            throw new ValidationException( errorMsg );
        }
        return new DataMapper().mapStaffTable(depId, staff, dob, startDay);
    }

    private Date convertDates(String date, String valueName, List<String> buildResponseList) {
        try {
            if(commonConv.stringIsNullOrEmpty(date)) {
                buildResponseList.add("Mandatory " + valueName.toLowerCase() + " is missing.");
                return null;
            }
            return commonConv.getDateFromString(date);
        } catch ( ParseException e ) {
            buildResponseList.add(valueName + " is invalid. Try format yyyy-mm-dd.");
        }
        return null;
    }

    private List<String> validateStaffDetails(StaffLoginDetailsJson staff ) {
        List<String> errorMessage = new ArrayList<>();

        if (commonConv.stringIsNullOrEmpty(staff.getFullName())) {
            errorMessage.add("Mandatory name is missing.");
        }

        if (commonConv.stringIsNullOrEmpty(staff.getPosition())) {
            errorMessage.add("Mandatory position is missing.");
        }

        if ( !commonConv.stringIsNullOrEmpty(staff.getStaffEmail())) {
          if ( !commonConv.doesEmailIsValid(staff.getStaffEmail())) {
              errorMessage.add("Invalid staff email.");
          }
        }

        return errorMessage;
    }

}
