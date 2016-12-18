package com.departments.ipa.dep_dbo_interface;

import com.departments.ipa.data.Department;
import com.departments.ipa.fault.exception.SQLFaultException;

import java.util.List;

/**
 * Created by david on 18/09/16.
 */
public interface DepartmentDBOInter {

    List<Department> getDepartments() throws SQLFaultException;

    boolean checkDepartmenName(String depName) throws SQLFaultException;

    void modifyDepartmentName(Integer depId, String name) throws SQLFaultException;

    Department getDepartment(Integer depId) throws SQLFaultException;

    void createNewDepartment(String depName, Integer creater) throws SQLFaultException;
}
