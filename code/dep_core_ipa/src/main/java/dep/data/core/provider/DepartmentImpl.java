package dep.data.core.provider;


import com.departments.dto.data.Department;
import com.departments.dto.dep_dbo.DepartmentDBO;
import com.departments.dto.fault.exception.SQLFaultException;
import dep.data.core.provider.inter.provider.DepartmentInter;

import java.util.List;

public class DepartmentImpl implements DepartmentInter {

    private final DepartmentDBO depDBO;

    public DepartmentImpl(DepartmentDBO depDbo){
        this.depDBO = depDbo;
    }

    public List<Department> getDepartmentList() throws SQLFaultException {
        List<Department> depList =  depDBO.getDepartments();
        return depList;
    }

    public Boolean checkDepartmenName(String name) throws SQLFaultException {
        return depDBO.checkDepartmenName(name);
    }

    public Department getDepartment(Integer depId) throws SQLFaultException {
        return depDBO.getDepartment(depId);
    }

    public void createNewDepartment(String depName, Integer creater) throws SQLFaultException {
        depDBO.createNewDepartment(depName, creater);
    }
    public void modifyDepartmentName(Integer depId, String name) throws SQLFaultException {
        depDBO.modifyDepartmentName(depId, name);
    }
}