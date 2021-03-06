package dep.data.core.provider.inter.provider;

import com.departments.dto.data.Department;
import com.departments.dto.fault.exception.SQLFaultException;
import com.departments.dto.fault.exception.ValidationException;

import java.util.List;

/**
 * Created by david on 18/09/16.
 */
public interface DepartmentInter {

    List<Department> getDepartmentList() throws SQLFaultException;

    Boolean checkDepartmenName(String name) throws SQLFaultException;

    boolean doesDepartmentExist(int depId) throws SQLFaultException, ValidationException;

    Department getDepartment(Integer depId) throws SQLFaultException;

    void createNewDepartment(String depName, Integer creater) throws SQLFaultException;

    void modifyDepartmentName(Integer depId, String name) throws SQLFaultException;
}
