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

    // http://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
    // http://stackoverflow.com/questions/18142745/how-do-i-generate-a-salt-in-java-for-salted-hash
    public LoginStaff logInUser(String email, String password) throws DepartmentFaultService {
        Integer userId = userDBO.loninUser(email, password);
        if ( userId == null ) return null;
        loginStaff = userDBO.getStaffDetails(userId);
        return loginStaff;
    }
}
