package dep.data.provider;

import com.department.core.config.DepartmentProperties;
import com.departments.ipa.data.DepartmentDbTable;
import com.departments.ipa.dep_dbo.DepartmentDBO;
import com.departments.ipa.dep_dbo.DepartmentDBOConnection;
import dep.data.core_dep.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentImpl {

    private final DepartmentDBOConnection depConn;
    private final DepartmentDBO depDBO;

    public DepartmentImpl(){
//        DepartmentConfigLoader dc = new DepartmentConfigLoader();
        depConn = new DepartmentDBOConnection(new DepartmentProperties().getPropertiesDataConfig());
        depDBO = new DepartmentDBO();
    }

    public List<Department> getDepartmentList(){

        List<DepartmentDbTable> depList =  depDBO.getDepartments(depConn.getDbConnection());
        List<Department> list = new ArrayList<Department>();
        for(DepartmentDbTable dtl : depList) {
            String creater = depDBO.getCreaterName(dtl.getId(), depConn.getDbConnection());
            list.add(new Department(dtl.getId(), dtl.getName(), creater));

        }
        Department dep = new Department(1,"IT Developer", "David Sajdl");
        Department dep1 = new Department(2,"IT Designer", "David Sajdl");
        Department dep2 = new Department(3,"IT Consultant", "David Sajdl");

        list.add(dep);
        list.add(dep1);
        list.add(dep2);
        return list;
    }
}