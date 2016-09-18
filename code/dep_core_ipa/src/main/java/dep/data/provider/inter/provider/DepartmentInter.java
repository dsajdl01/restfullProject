package dep.data.provider.inter.provider;

import com.departments.ipa.data.Department;
import com.departments.ipa.fault.exception.DepartmentFaultService;

import java.util.List;

/**
 * Created by david on 18/09/16.
 */
public interface DepartmentInter {

    List<Department> getDepartmentList() throws DepartmentFaultService;

    Boolean checkDepartmenName(String name) throws DepartmentFaultService;

    void createNewDepartment(String depName, Integer creater) throws DepartmentFaultService;

    void modifyDepartmentName(Integer depId, String name) throws DepartmentFaultService;
}
