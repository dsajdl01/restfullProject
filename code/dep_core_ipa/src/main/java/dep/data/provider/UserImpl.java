package dep.data.provider;


import com.departments.ipa.data.LoginStaff;
import com.departments.ipa.dep_dbo.UserDBO;
import com.departments.ipa.fault.exception.DepartmentFaultService;
import dep.data.provider.inter.provider.UserInter;

/**
 * Created by david on 13/11/16.
 */
public class UserImpl implements UserInter {

    private  LoginStaff loginStaff;

    private UserDBO userDBO;

    public UserImpl(UserDBO userDBO) {
        this.userDBO = userDBO;
        this.loginStaff = null;
    }

    public LoginStaff logInUser(String email, String password) throws DepartmentFaultService {
        Integer userId = userDBO.loninUser(email, password);
        if ( userId == null ) return null;
        loginStaff = userDBO.getStaffDetails(userId);
        return loginStaff;
    }
}
