package dep.data.core.provider.inter.provider;

import com.departments.dto.common.lgb.SearchType;
import com.departments.dto.data.LoginDetails;
import com.departments.dto.data.LoginStaff;
import com.departments.dto.data.Staff;
import com.departments.dto.fault.exception.SQLFaultException;
import com.departments.dto.fault.exception.ValidationException;

import java.util.List;

/**
 * Created by david on 19/11/16.
 */
public interface UserInter {

    LoginStaff logInUser(String email, String password) throws SQLFaultException;

    void saveNewStaff(Staff staff) throws SQLFaultException;

    void saveNewStaffAndLoginDetails(Staff staff, LoginDetails loginDetail) throws SQLFaultException;

    void saveLoginDetails(LoginDetails loginDetail, int staffId) throws SQLFaultException;

    void modifyStaffDetails(Staff modifyStaff) throws SQLFaultException, ValidationException;

    List<Staff> searchForStaffs(int depId, String value, SearchType type) throws SQLFaultException, ValidationException;

    boolean doesEmailExist(String email) throws SQLFaultException;
}
