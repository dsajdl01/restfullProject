package dep.data.provider;


import com.departments.ipa.data.LoginStaff;
import com.departments.ipa.dep_dbo.UserDBO;
import com.departments.ipa.fault.exception.DepartmentFaultService;

/**
 * Created by david on 13/11/16.
 */
public class UserImpl {

    private UserDBO userDBO;

    public UserImpl(UserDBO userDBO) {
        this.userDBO = userDBO;
    }

    public LoginStaff logInUser(String email, String password) throws DepartmentFaultService {
        Integer userId = userDBO.loninUser(email, password);
        if ( userId == null ) return null;
        return userDBO.getStaffDetails(userId);
    }
}
