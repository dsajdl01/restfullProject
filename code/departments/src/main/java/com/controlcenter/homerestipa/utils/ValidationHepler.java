package com.controlcenter.homerestipa.utils;

import com.controlcenter.homerestipa.response.DepartmentJson;
import com.controlcenter.homerestipa.response.StaffJson;
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
public class ValidationHepler {

    private CommonConversions commonConv = new CommonConversions();
    private PasswordAuthentication passwordAuth;

    private static final int MAX_PASSWORD_LENGTH = 8;
    private static final int MAX_SEARCH_VALUE = 3;
    private static final int MAX_NAME_VALUE = 4;

    public ValidationHepler(PasswordAuthentication passwordAuth) {
        this.passwordAuth = passwordAuth;
    }

    public void basicStaffValidation(Integer depId, StaffLoginDetailsJson newStaff) throws ValidationException {
        basicValidationOfDepartmentId(depId);
        basicValidateStaffObject(newStaff);
    }

    public void basicValidationOfSearchValue(String val) throws ValidationException {
        if ( commonConv.stringIsNullOrEmpty(val) || !commonConv.hasStringMaxLength(val, MAX_SEARCH_VALUE) ) {
            throw new ValidationException("Search value mas be at least " + MAX_SEARCH_VALUE + " characters" );
        }
    }

    public void basicDepartmentValidation(DepartmentJson dep ) throws ValidationException {
        if ( dep == null || commonConv.stringIsNullOrEmpty(dep.getDepName())) {
            throw new  ValidationException("Mandatory argument department name is missing");
        }
    }

    public void basicValidationOfDepartmentId(Integer depId) throws ValidationException {
        if (depId == null ) {
            throw new ValidationException("Mandatory department ID is missing.");
        } else if ( depId <= 0 ) {
            throw new ValidationException("Invalid department ID.");
        }
    }

    public void basicStaffValidation(StaffJson staff) throws  ValidationException{
        if ( staff == null ) {
            throw new ValidationException("Mandatory staff object is missing.");
        }
    }

    public Staff validateMandatoryStaffDetailsAndMapStaff(StaffJson staff) throws ValidationException{
        basicValidationOfDepartmentId(staff.getDepId());
        List<String> valResponse = validateMandatoryStaffDetails(staff.getFullName(), staff.getPosition(), staff.getStaffEmail());
        Date dob = convertDates(staff.getDob(), "Date of birthday", valResponse);
        Date startDay = convertDates(staff.getStartDay(), "Start day", valResponse);
        Date lastDay =  convertLastDay(staff.getLastDay(), "Last Day", valResponse);
        processValidationResponse(valResponse);
        return new DataMapper().mapStaffDetails(staff, dob, startDay, lastDay);
    }

    public void  basicValidateStaffObject(StaffLoginDetailsJson staff) throws ValidationException {
        if ( staff == null ) {
            throw new ValidationException("Mandatory staff object is missing.");
        }
    }

    public LoginDetails validateAndGetLoginDetails(String email, String password) throws ValidationException {
        validateEmailAndPassword(email, password);
        return new DataMapper().mapLoginDetails(email,passwordAuth.hashPassword(password));
    }

    public void basicValidateEmailAndPasswordLogin(String email, String password) throws ValidationException {
        try {
            validateEmailAndPassword(email, password);
        } catch (ValidationException e) {
            throw new ValidationException("Mandatory argument email or password are missing");
        }
    }

    public void validateEmailAndPassword(String email, String password) throws ValidationException {
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
    }

    public Staff validateAndGetStaffDetails(int depId, StaffLoginDetailsJson staff) throws  ValidationException {

        List<String> valResponseList = validateMandatoryStaffDetails(staff.getFullName(), staff.getPosition(), staff.getStaffEmail());
        Date dob = convertDates(staff.getDob(), "Date of birthday", valResponseList);
        Date startDay = convertDates(staff.getStartDay(), "Start day", valResponseList);
        processValidationResponse(valResponseList);
        return new DataMapper().mapStaffTable(depId, staff, dob, startDay);
    }

    private void processValidationResponse(List<String> valResponseList) throws ValidationException {
        String errorMsg = "";
        if ( !valResponseList.isEmpty() ) {
            for ( String msg : valResponseList ) {
                errorMsg += msg + " ";
            }
            throw new ValidationException( errorMsg );
        }
    }

    private Date convertDates(String date, String valueName, List<String> buildResponseList) {

        if(commonConv.stringIsNullOrEmpty(date)) {
             buildResponseList.add("Mandatory " + valueName.toLowerCase() + " is missing.");
             return null;
        }
        return  convertLastDay(date, valueName, buildResponseList);
    }

    private Date convertLastDay(String date, String valueName, List<String> buildResponseList) {
        try {
            if(!commonConv.stringIsNullOrEmpty(date)) {
                return commonConv.getDateFromString(date);
            }
        } catch ( ParseException e ) {
            buildResponseList.add(valueName + " is invalid. Try format yyyy-mm-dd.");
        }
        return null;
    }

    private List<String> validateMandatoryStaffDetails(String name, String position, String email) {
        List<String> errorMessage = new ArrayList<>();

        if (commonConv.stringIsNullOrEmpty(name)) {
            errorMessage.add("Mandatory name is missing.");
        } else if (!commonConv.hasStringMaxLength(name, MAX_NAME_VALUE)) {
            errorMessage.add("Full name must be at least " + MAX_NAME_VALUE + " characters long");
        }

        if (commonConv.stringIsNullOrEmpty(position)) {
            errorMessage.add("Mandatory position is missing.");
        }

        if ( !commonConv.stringIsNullOrEmpty(email)) {
          if ( !commonConv.doesEmailIsValid(email)) {
              errorMessage.add("Invalid staff email.");
          }
        }

        return errorMessage;
    }

}
