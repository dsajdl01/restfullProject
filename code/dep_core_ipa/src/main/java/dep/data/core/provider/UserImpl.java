package dep.data.core.provider;


import com.department.core.data.PasswordAuthentication;
import com.departments.dto.common.lgb.CommonConversions;
import com.departments.dto.data.LoginDetails;
import com.departments.dto.data.LoginStaff;
import com.departments.dto.data.StaffTable;
import com.departments.dto.dep_dbo.UserDBO;
import com.departments.dto.fault.exception.SQLFaultException;
import dep.data.core.provider.inter.provider.UserInter;

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
        loginStaff = userDBO.getStaffDetails(userId);
        return loginStaff;
    }

    public void saveNewStaff(StaffTable staff) throws SQLFaultException {
        userDBO.saveNewStaff(staff);

    }

    public void saveLoginDetails(LoginDetails loginDetail) throws SQLFaultException {
        userDBO.saveLoginDetails(loginDetail);
    }

    public boolean doesEmailExist(String email) throws SQLFaultException {
        return userDBO.doesEmailExist(email);
    }
}
