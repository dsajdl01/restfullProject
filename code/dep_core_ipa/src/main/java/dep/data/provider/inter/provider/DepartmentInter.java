package dep.data.provider.inter.provider;

import com.departments.ipa.data.Department;
import com.departments.ipa.fault.exception.SQLFaultException;

import java.util.List;

/**
 * Created by david on 18/09/16.
 */
public interface DepartmentInter {

    List<Department> getDepartmentList() throws SQLFaultException;

    Boolean checkDepartmenName(String name) throws SQLFaultException;

    Department getDepartment(Integer depId) throws SQLFaultException;

    void createNewDepartment(String depName, Integer creater) throws SQLFaultException;

    void modifyDepartmentName(Integer depId, String name) throws SQLFaultException;
}
