package dep.data.provider;


import com.departments.ipa.data.Department;
import com.departments.ipa.dep_dbo.DepartmentDBO;
import com.departments.ipa.fault.exception.DepartmentFaultService;

import java.util.List;

public class DepartmentImpl {

    private final DepartmentDBO depDBO;

    public DepartmentImpl(DepartmentDBO depDbo){
        this.depDBO = depDbo;
    }

    public List<Department> getDepartmentList() throws DepartmentFaultService {
        List<Department> depList =  depDBO.getDepartments();
        return depList;
    }
}