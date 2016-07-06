package dep.data.provider;

import dep.data.core_dep.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentImpl {

    public DepartmentImpl(){

    }

    public List<Department> getDepartmentList(){
        Department dep = new Department(1,"IT Developer", 1);
        Department dep1 = new Department(2,"IT Designer", 1);
        Department dep2 = new Department(3,"IT Consultant", 1);
        List<Department> list = new ArrayList<Department>();
        list.add(dep);
        list.add(dep1);
        list.add(dep2);
        return list;
    }
}