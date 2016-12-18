package dep.data.provider;


import com.departments.ipa.data.Department;
import com.departments.ipa.dep_dbo.DepartmentDBO;
import com.departments.ipa.fault.exception.SQLFaultException;
import dep.data.provider.inter.provider.DepartmentInter;

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