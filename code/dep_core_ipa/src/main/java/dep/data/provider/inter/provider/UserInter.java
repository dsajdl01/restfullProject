package dep.data.provider.inter.provider;

import com.departments.ipa.data.LoginStaff;
import com.departments.ipa.fault.exception.SQLFaultException;

/**
 * Created by david on 19/11/16.
 */
public interface UserInter {

    LoginStaff logInUser(String email, String password) throws SQLFaultException;

    boolean doesEmailExist(String email) throws SQLFaultException;
}
