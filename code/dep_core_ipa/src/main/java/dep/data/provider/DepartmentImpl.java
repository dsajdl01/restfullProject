package dep.data.provider;


import com.departments.ipa.data.Department;
import com.departments.ipa.dep_dbo.DepartmentDBO;

import java.util.List;

public class DepartmentImpl {

    private final DepartmentDBO depDBO;

    public DepartmentImpl(DepartmentDBO depDbo){
        this.depDBO = depDbo;
    }

    public List<Department> getDepartmentList(){
        List<Department> depList =  depDBO.getDepartments();
        return depList;
    }
}