package dep.data.provider;

import com.department.core.config.DepartmentProperties;
import com.departments.ipa.data.DepartmentDbTable;
import com.departments.ipa.dep_dbo.DepartmentDBO;
import com.departments.ipa.dep_dbo.DepartmentDBOConnection;
import dep.data.core_dep.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentImpl {

    private final DepartmentDBO depDBO;

    public DepartmentImpl(){
        depDBO = new DepartmentDBO(new DepartmentDBOConnection(new DepartmentProperties().getPropertiesDataConfig()).getDbConnection());
    }

    public List<Department> getDepartmentList(){

        List<DepartmentDbTable> depList =  depDBO.getDepartments();
        List<Department> list = new ArrayList<Department>();
        for(DepartmentDbTable dtl : depList) {
            String creater = depDBO.getCreaterName(dtl.getCreatedBy());
            list.add(new Department(dtl.getId(), dtl.getName(), creater));

        }
        list.add(new Department(1,"IT Developer", "Jolita Best"));
        list.add(new Department(2,"IT Designer", "Yadira Diez"));
        list.add(new Department(3,"IT Consultant", "David Sajdl"));

        return list;
    }
}