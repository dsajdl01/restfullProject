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
import com.httpSession.core.HttpSessionCoreServlet;
import dep.data.core.provider.inter.provider.UserInter;

import javax.servlet.http.HttpSession;
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
    private HttpSessionCoreServlet httpSessionCoreServlet;

    public UserImpl(UserDBO userDBO, PasswordAuthentication passwordAuth, HttpSessionCoreServlet httpSessionCoreServlet) {
        this.userDBO = userDBO;
        this.passwordAuth = passwordAuth;
        this.loginStaff = null;
        this.httpSessionCoreServlet = httpSessionCoreServlet;
    }

    // http://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
    // http://stackoverflow.com/questions/18142745/how-do-i-generate-a-salt-in-java-for-salted-hash
    @Override
    public LoginStaff logInUser(String email, String password, HttpSession session) throws SQLFaultException, ValidationException {
        String encryptPassword = userDBO.getLoginStaffPassword(email);
        if (!commonConv.stringIsNullOrEmpty(encryptPassword) ) {
            boolean authenticate = passwordAuth.authenticate(password, encryptPassword);
            if (authenticate) {
                final LoginStaff loginStaffDetails = getLoginStaffDetails(email);
                httpSessionCoreServlet.setStaffIdAttribute(loginStaffDetails.getUserId(), session);
                return loginStaffDetails;
            }
        }
        session.invalidate();
        throw new ValidationException("Invalid email or password");
    }

    private LoginStaff getLoginStaffDetails(String email) throws SQLFaultException {
        Integer userId = userDBO.loninUserId(email);
        if (userId == null) return null;
        return userDBO.getLogInStaffDetails(userId);
    }

    public Staff getStaffDetails(int staffId) throws SQLFaultException {
       return userDBO.getStaffDetails(staffId);
    }

    @Override
    public void saveNewStaffAndLoginDetails(Staff staff, LoginDetails loginDetail) throws SQLFaultException {
        saveNewStaff(staff);
        final int newStaffId = getNewStaffId();
        if ( newStaffId <= 0 ) throw new SQLFaultException("Error occur when getting new staff id");

        saveLoginDetails(loginDetail, newStaffId);
    }

    public int getNewStaffId() throws SQLFaultException {
        return userDBO.getNewStaffId();
    }

    public void deleteStaff(int staffId) throws SQLFaultException{
        userDBO.deleteStaffLoginDetails(staffId);
        userDBO.deleteStaffDetails(staffId);
    }

    @Override
    public void saveNewStaff(Staff staff) throws SQLFaultException {
        userDBO.saveNewStaff(staff);
    }

    @Override
    public void saveLoginDetails(LoginDetails loginDetail, int staffId) throws SQLFaultException {
        userDBO.saveLoginDetails(loginDetail, staffId);
    }

    @Override
    public void modifyStaffDetails(Staff modifyStaff) throws SQLFaultException, ValidationException  {
        final Staff staff = checkIfStaffExist(modifyStaff.getId());
        if (staff == null ) throw new ValidationException("Staff id " + modifyStaff.getId() + " does not exist for staff name " + modifyStaff.getName());
        departmentIdMatch(modifyStaff.getDepId(), staff.getDepId());
        userDBO.modifyStaff(modifyStaff);
    }

    protected void departmentIdMatch(int modifyStaffDepId, int existingStaffId) throws ValidationException {
        if ( modifyStaffDepId != existingStaffId ) {
            throw  new ValidationException("Department id does not match.");
        }
    }

    @Override
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

    public Staff checkIfStaffExist(int staffId) throws  SQLFaultException, ValidationException {
        return userDBO.checkIfStaffExist(staffId);
    }

    private void validateDate(String date) throws ValidationException {
        try {
            commonConv.getDateFromString(date);
        } catch ( ParseException e ) {
            throw new ValidationException("Invalid date of birthday: " + date );
        }
    }

    @Override
    public boolean doesEmailExist(String email) throws SQLFaultException {
        return userDBO.doesEmailExist(email);
    }
}
