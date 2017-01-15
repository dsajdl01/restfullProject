package com.controlcenter.homerestipa.utils;

import com.controlcenter.homerestipa.response.DepartmentJson;
import com.controlcenter.homerestipa.response.StaffJson;
import com.controlcenter.homerestipa.response.StaffLoginDetailsJson;
import com.department.core.data.PasswordAuthentication;
import com.departments.dto.common.lgb.CommonConversions;
import com.departments.dto.data.LoginDetails;
import com.departments.dto.data.Staff;
import com.departments.dto.fault.exception.ValidationException;
import com.httpSession.core.HttpSessionCoreServlet;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by david on 25/12/16.
 */
public class ValidationHelperTest {

    PasswordAuthentication passAuth = new PasswordAuthentication( new HttpSessionCoreServlet());
    ValidationHelper validationHelper = new ValidationHelper(passAuth);
    private CommonConversions commonConv = new CommonConversions();

    @Test
    public void basicStaffValidation_Test() {
        try {
            validationHelper.basicStaffValidation(3, generateStaff("Full Name", "1999-02-02", "2016-08-09", "Developer", "satff@email.com"));
        } catch (ValidationException e) {
            fail( "basicStaffValidation_Test: should not be thrown ValidationException: " + e.getMessage());
        }
    }

    @Test
    public void basicStaffValidationErrorDepId_Test() {
        try {
            validationHelper.basicStaffValidation(-3, generateStaff("Full Name", "1999-02-02", "2016-08-09", "Developer", "satff@email.com"));
            fail( "basicStaffValidationErrorDepId_Test: should be throw ValidationException: ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Invalid department ID."));
        }
    }

    @Test
    public void basicStaffValidationErrorNull_Test() {
        try {
            validationHelper.basicStaffValidation(3, null);
            fail( "basicStaffValidationErrorDepId_Test: should be throw ValidationException: ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Mandatory staff object is missing."));
        }
    }

    @Test
    public void validateAndGetStaffDetailsTest() {
        String email = "email@example.com";
       try {
           LoginDetails loginDetails = validationHelper.validateAndGetLoginDetails(email, "password123");
           assertThat(loginDetails.getEmail(), is(email));
       } catch (ValidationException e) {
           fail( "validateAndGetStaffDetailsTest should not throw ValidationException: " + e.getMessage());
       }
    }

    @Test
    public void validateAndGetStaffDetailsMissingEmailTest() {
        try {
            validationHelper.validateAndGetLoginDetails(null, "password123");
            fail( "validateAndGetStaffDetailsTest: ValidationException should be thrown here.");
        } catch (ValidationException e) {
            String mm = e.getMessage();
            assertThat(mm, is("Mandatory login email is missing. "));
        }
    }

    @Test
    public void validateAndGetStaffDetailsMissingInvalidEmailTest() {
        try {
            validationHelper.validateAndGetLoginDetails("email@eemail@com", "password123");
            fail( "validateAndGetStaffDetailsTest: ValidationException should be thrown here.");
        } catch (ValidationException e) {
            String mm = e.getMessage();
            assertThat(mm, is("Invalid email. "));
        }
    }

    @Test
    public void validateAndGetStaffDetailsMissingEmailAndPasswordTest() {
        try {
            validationHelper.validateAndGetLoginDetails(null, null);
            fail( "validateAndGetStaffDetailsTest: ValidationException should be thrown here.");
        } catch (ValidationException e) {
            String mm = e.getMessage();
            assertThat(mm, is("Mandatory login email is missing. Mandatory password is missing. "));
        }
    }

    @Test
    public void validateAndGetStaffDetailsPasswordToShortTest() {
        try {
            validationHelper.validateAndGetLoginDetails("soem@email.com", "pass123");
            fail( "validateAndGetStaffDetailsTest: ValidationException should be thrown here.");
        } catch (ValidationException e) {
            String mm = e.getMessage();
            assertThat(mm, is("Password must be at least 8 characters long."));
        }
    }

    @Test
    public void validateAndGetStaffDetails_Test() {
        try {
            Staff staff = validationHelper.validateAndGetStaffDetails(2, generateStaff("Full Name", "1999-01-01", "2016-01-01", "Manager", "staff@email.com"));
            assertThat(staff.getName(), is("Full Name"));
            assertThat(staff.getPosition() , is("Manager"));
            assertThat(staff.getEmail(), is("staff@email.com"));
            assertThat(commonConv.convertDateToString(staff.getDob()), is("1999-01-01"));
            assertThat(commonConv.convertDateToString(staff.getStartDay()), is("2016-01-01"));
        } catch (ValidationException e) {
            fail( "validateAndGetStaffDetails: ValidationException should NOT be thrown here. " + e.getMessage());
        }
    }

    @Test
    public void validateAndGetStaffDetailsMissingDates_Test(){
        try {
            validationHelper.validateAndGetStaffDetails(2, generateStaff("Full Name", null, null, "Manager", "staff@email.com"));
            fail( "validateAndGetStaffDetailsMissingDates_Test: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Mandatory date of birthday is missing. Mandatory start day is missing. "));
        }
    }

    @Test
    public void validateAndGetStaffDetailsInvalidEmail_Test(){
        try {
            validationHelper.validateAndGetStaffDetails(2, generateStaff("Full Name", "1999-01-01", "2016-01-01", "Manager", "staff@email@co.uk"));
            fail( "validateAndGetStaffDetailsInvalidEmail_Test: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Invalid staff email. "));
        }
    }

    @Test
    public void validateAndGetStaffDetailsInvaliDOB_Test(){
        try {
            validationHelper.validateAndGetStaffDetails(2, generateStaff("Full Name", "1999/01/01", "2016-01-01", "Manager", "staff@email.co.uk"));
            fail( "validateAndGetStaffDetailsInvalidEmail_Test: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Date of birthday is invalid. Try format yyyy-mm-dd. "));
        }
    }

    @Test
    public void validateAndGetStaffDetailsInvaliStartDay_Test(){
        try {
            validationHelper.validateAndGetStaffDetails(2, generateStaff("Full Name", "1999-01-01", "2016.01.01", "Manager", "staff@email.co.uk"));
            fail( "validateAndGetStaffDetailsInvalidEmail_Test: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Start day is invalid. Try format yyyy-mm-dd. "));
        }
    }

    @Test
    public void validateAndGetStaffDetailsFullNameAndPositionMissing_Test(){
        try {
            validationHelper.validateAndGetStaffDetails(2, generateStaff(null, "1999-01-01", "2016-01-01", null, "staff@email.co.uk"));
            fail( "validateAndGetStaffDetailsInvalidEmail_Test: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Mandatory name is missing. Mandatory position is missing. "));
        }
    }

    @Test
    public void basicValidateStaffObject_Test() {
        try {
            validationHelper.basicValidateStaffObject(generateStaff("Full Name", "1999-01-01", "2016-01-01", "Manager", "staff@email.co.uk"));
        } catch (ValidationException e) {
            fail( "basicValidateStaffObject_Test: ValidationException should be thrown here. " + e.getMessage());
        }
    }

    @Test
    public void basicValidateStaffObjectError_Test() {
        try {
            validationHelper.basicValidateStaffObject(null);
            fail( "basicValidateStaffObjectError: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Mandatory staff object is missing."));
        }
    }

    @Test
    public void basicValidateDepartmentId_Test() {
        try {
            validationHelper.basicValidationOfDepartmentId(4);
        } catch (ValidationException e) {
            fail( "basicValidateDepartmentId_Test: ValidationException should NOT be thrown here. " + e.getMessage());
        }
    }

    @Test
    public void basicValidateDepartmentIdNull_Test() {
        try {
            validationHelper.basicValidationOfDepartmentId(null);
            fail( "basicValidateDepartmentIdNull_Test: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Mandatory department ID is missing."));
        }
    }

    @Test
    public void basicValidateDepartmentIdZero_Test() {
        try {
            validationHelper.basicValidationOfDepartmentId(0);
            fail( "basicValidateDepartmentIdNull_Test: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Invalid department ID."));
        }
    }

    @Test
    public void basicValidationOfSearchValue_Test() {
        try {
            validationHelper.basicValidationOfSearchValue("david");
        } catch (ValidationException e) {
            fail( "basicValidationOfSearchValue_Test: ValidationException should NOT be thrown here. " + e.getMessage());
        }
    }

    @Test
    public void basicValidationOfSearchValueNullException_Test() {
        try {
            validationHelper.basicValidationOfSearchValue(null);
            fail( "basicValidationOfSearchValueNullException_Test: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Search value mas be at least 3 characters"));
        }
    }

    @Test
    public void basicValidationOfSearchValueException_Test() {
        try {
            validationHelper.basicValidationOfSearchValue("da");
            fail( "basicValidationOfSearchValueException_Test: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Search value mas be at least 3 characters"));
        }
    }

    @Test
    public void basicStaffValidationJson_Test() {
        try {
            validationHelper.basicStaffValidation(generateStaffJson("Yadira Diez", "Java Developer", "yadira@diez.com"));
        } catch (ValidationException e) {
            fail( "basicStaffValidationJson_Test: ValidationException should NOT be thrown here. ");
        }
    }

    @Test
    public void basicStaffValidationJsonNull_Test() {
        try {
            validationHelper.basicStaffValidation(null);
            fail( "basicStaffValidationJsonNull_Test: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Mandatory staff object is missing."));
        }
    }

    @Test
    public void validateMandatoryStaffDetailsAndMapStaff_Test() throws  Exception {
        Staff staff = validationHelper.validateMandatoryStaffDetailsAndMapStaff(generateStaffJson("Yadira Diez", "Java Developer", "yadira.diez@example.com"));
        assertThat(staff.getName(), is("Yadira Diez"));
        assertThat(staff.getPosition(), is("Java Developer"));
        assertThat(staff.getEmail(), is("yadira.diez@example.com"));
    }

    @Test
    public void validateMandatoryStaffDetailsAndMapStaffValError_Test() throws  Exception {
        try {
            Staff staff = validationHelper.validateMandatoryStaffDetailsAndMapStaff(generateStaffJson("Die", "Java Developer", "yadira.diez@example.com"));
            fail( "validateMandatoryStaffDetailsAndMapStaffValError_Test: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Full name must be at least 4 characters long "));
        }
    }

    @Test
    public void basicDepartmentValidation_Test() throws Exception {
        try {
            validationHelper.basicDepartmentValidation( new DepartmentJson(2, "IT", "Bob Fred"));
        } catch (ValidationException e) {
            fail( "basicDepartmentValidation_Test: ValidationException should NOT be thrown here. ");
        }
    }

    @Test
    public void basicDepartmentValidationError_Test() throws Exception {
        try {
            validationHelper.basicDepartmentValidation( new DepartmentJson(2, "", "Bob Fred"));
            fail( "basicDepartmentValidationError_Test: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Mandatory argument department name is missing"));
        }
    }

    @Test
    public void basicDepartmentValidationErrorNull_Test() throws Exception {
        try {
            validationHelper.basicDepartmentValidation( null );
            fail( "basicDepartmentValidationErrorNull_Test: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Mandatory argument department name is missing"));
        }
    }

    @Test
    public void basicValidateEmailAndPasswordLogin_Test() throws Exception {
        try {
            validationHelper.basicValidateEmailAndPasswordLogin("some@email.com", "password123");
        } catch (ValidationException e) {
            fail( "basicValidateEmailAndPasswordLogin_Test: ValidationException should NOT be thrown here. ");
        }
    }

    @Test
    public void basicValidateEmailAndPasswordLoginError_Test() throws Exception {
        try {
            validationHelper.basicValidateEmailAndPasswordLogin("some@email@com", "password123");
            fail( "basicValidateEmailAndPasswordLogin_Test: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Mandatory argument email or password are missing"));
        }
    }

    private StaffJson generateStaffJson(String name, String position, String email) {
        return new StaffJson(1, 2, name, "1990-01-10", "2016-10-26",null,
                position, email, null);
    }

    private StaffLoginDetailsJson generateStaff(String name, String dob, String startDay, String position, String staffEmail) {
        return generateStaff(name, dob, startDay, position, staffEmail, "loging@email.com", "somePassword123");
    }

    private StaffLoginDetailsJson generateStaff(String name, String dob, String startDay, String position, String staffEmail, String loginEmail, String loginPassword) {
        return new StaffLoginDetailsJson(name, dob, startDay, position, staffEmail, null, loginEmail, loginPassword);
    }
}
