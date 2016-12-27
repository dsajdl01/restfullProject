package dep.data.core.provider;


import com.department.core.data.PasswordAuthentication;
import com.departments.dto.common.lgb.CommonConversions;
import com.departments.dto.common.lgb.SearchType;
import com.departments.dto.data.LoginDetails;
import com.departments.dto.data.LoginStaff;
import com.departments.dto.data.Staff;
import com.departments.dto.dep_dbo.UserDBO;
import com.departments.dto.fault.exception.SQLFaultException;
import com.departments.dto.fault.exception.ValidationException;
import dep.data.core.provider.inter.provider.UserInter;

import java.text.ParseException;
import java.util.List;

/**
 * Created by david on 13/11/16.
 */
public class UserImpl implements UserInter {

    private  LoginStaff loginStaff;

    private UserDBO userDBO;
    private PasswordAuthentication passwordAuth;
    private CommonConversions commonConv = new CommonConversions();

    public UserImpl(UserDBO userDBO, PasswordAuthentication passwordAuth) {
        this.userDBO = userDBO;
        this.passwordAuth = passwordAuth;
        this.loginStaff = null;
    }

    // http://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
    // http://stackoverflow.com/questions/18142745/how-do-i-generate-a-salt-in-java-for-salted-hash
    public LoginStaff logInUser(String email, String password) throws SQLFaultException {
        String encryptPassword = userDBO.getLoginStaffPassword(email);
        if (!commonConv.stringIsNullOrEmpty(encryptPassword) ) {
            boolean authenticate = passwordAuth.authenticate(password, encryptPassword);
            if (authenticate) {
                return getLoginStaffDetails(email);
            }
        }
        return null;
    }

    private LoginStaff getLoginStaffDetails(String email) throws SQLFaultException {
        Integer userId = userDBO.loninUserId(email);
        if (userId == null) return null;
        loginStaff = userDBO.getLogInStaffDetails(userId);
        return loginStaff;
    }

    public Staff getStaffDetails(int staffId) throws SQLFaultException {
       return userDBO.getStaffDetails(staffId);
    }

    public void saveNewStaffAndLoginDetails(Staff staff, LoginDetails loginDetail) throws SQLFaultException {
        saveNewStaff(staff);
        final int newStaffId = getNewStaffId();
        if ( newStaffId <= 0 ){
            throw new SQLFaultException("Error occur when getting new staff id");
        }
        saveLoginDetails(loginDetail, newStaffId);
    }

    public int getNewStaffId() throws SQLFaultException {
        return userDBO.getNewStaffId();
    }

    public void deleteStaff(int staffId) throws SQLFaultException{
        userDBO.deleteStaffLoginDetails(staffId);
        userDBO.deleteStaffDetails(staffId);
    }

    public void saveNewStaff(Staff staff) throws SQLFaultException {
        userDBO.saveNewStaff(staff);
    }

    public void saveLoginDetails(LoginDetails loginDetail, int staffId) throws SQLFaultException {
        userDBO.saveLoginDetails(loginDetail, staffId);
    }

    public List<Staff> searchForStaffs(int depId, String value, SearchType type) throws SQLFaultException, ValidationException {

        switch (type) {
            case NAME:
                return  userDBO.searchForStaffsByName(depId, value);
            case DOB:
                validateDate(value);
                return userDBO.searchForStaffsByDOB(depId, value);
            default:
                throw new ValidationException("Unknown search type");
        }
    }

    private void validateDate(String date) throws ValidationException {
        try {
            commonConv.getDateFromString(date);
        } catch ( ParseException e ) {
            throw new ValidationException("Invalid date of birthday: " + date );
        }
    }

    public boolean doesEmailExist(String email) throws SQLFaultException {
        return userDBO.doesEmailExist(email);
    }
}
