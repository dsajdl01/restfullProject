package dep.data.core.provider;


import com.departments.dto.data.Department;
import com.departments.dto.dep_dbo.DepartmentDBO;
import com.departments.dto.fault.exception.SQLFaultException;
import com.departments.dto.fault.exception.ValidationException;
import dep.data.core.provider.inter.provider.DepartmentInter;

import java.util.List;

public class DepartmentImpl implements DepartmentInter {

    private final DepartmentDBO depDBO;

    public DepartmentImpl(DepartmentDBO depDbo){
        this.depDBO = depDbo;
    }

    @Override
    public List<Department> getDepartmentList() throws SQLFaultException {
        List<Department> depList =  depDBO.getDepartments();
        return depList;
    }

    @Override
    public Boolean checkDepartmenName(String name) throws SQLFaultException {
        return depDBO.checkDepartmenName(name);
    }

    @Override
    public boolean doesDepartmentExist(int depId) throws SQLFaultException, ValidationException {
        boolean exist = depDBO.doesDepartmentExist(depId);
        if (!exist) throw new ValidationException("Department id does not exist");
        return exist;
    }

    @Override
    public Department getDepartment(Integer depId) throws SQLFaultException {
        return depDBO.getDepartment(depId);
    }

    @Override
    public void createNewDepartment(String depName, Integer creater) throws SQLFaultException {
        depDBO.createNewDepartment(depName, creater);
    }

    @Override
    public void modifyDepartmentName(Integer depId, String name) throws SQLFaultException {
        depDBO.modifyDepartmentName(depId, name);
    }
}