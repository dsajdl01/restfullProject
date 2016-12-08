package com.departments.ipa.dep_dbo_interface;

import com.departments.ipa.data.Department;
import com.departments.ipa.fault.exception.DepartmentFaultService;
import java.util.List;

/**
 * Created by david on 18/09/16.
 */
public interface DepartmentDBOInter {

    List<Department> getDepartments() throws DepartmentFaultService;

    boolean checkDepartmenName(String depName) throws DepartmentFaultService;

    void modifyDepartmentName(Integer depId, String name) throws DepartmentFaultService;

    Department getDepartment(Integer depId) throws DepartmentFaultService;

    void createNewDepartment(String depName, Integer creater) throws DepartmentFaultService;
}
