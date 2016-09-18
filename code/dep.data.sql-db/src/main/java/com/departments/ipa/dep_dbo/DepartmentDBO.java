package com.departments.ipa.dep_dbo;
//        com.departments/ipa/dep_dbo/DepartmentDBO.java

import com.departments.ipa.data.Department;
import com.departments.ipa.dep_dbo_interface.DepartmentDBOInter;
import com.departments.ipa.fault.exception.DepartmentFaultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class DepartmentDBO implements DepartmentDBOInter {

    private Connection con;

    protected static final Logger LOGGER = LoggerFactory.getLogger(DepartmentDBO.class);
    private static final String ALL_DEPARTMENT_QUERY = "select dep.id as id, dep.name as name, " +
            "staff.name as creater from department dep, staff staff where dep.createdBy = staff.dep_id and dep.name != 'root'";
    private static final String CHECK_DEP_NAME_QUARY = "select count(name) as numberOfDepNames from department where name like ?";
    private static final String ADD_NEW_DEPARTMENT_QUERY = "insert into department (name, createdBy) values (?, ?);";
    private static final String MODIFY_DEPARTMENT_NAME_QUERY = "UPDATE department SET name= ? where id = ?";

    public DepartmentDBO(Connection con){
        this.con = con;
    }

    public List<Department> getDepartments() throws  DepartmentFaultService {
        List<Department> departmentList = new ArrayList<Department>();
        PreparedStatement preparedStatement = null;
        try {

            preparedStatement = con.prepareStatement(ALL_DEPARTMENT_QUERY);
            ResultSet resSet = preparedStatement.executeQuery();
            while(resSet.next()){
                departmentList.add(new Department(resSet.getInt("id"), resSet.getString("name"), resSet.getString("creater")));
            }
            return  departmentList;
        } catch (SQLException sqlE){
            LOGGER.error("getDepartments: {}", sqlE);
            throw new DepartmentFaultService("Inable to connect to databese");
        }
    }

    public boolean checkDepartmenName(String depName) throws DepartmentFaultService {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(CHECK_DEP_NAME_QUARY);
            preparedStatement.setString(1, depName);
            ResultSet resSet = preparedStatement.executeQuery();
            resSet.next();
            return resSet.getInt("numberOfDepNames") == 0 ? false : true;
        }
            catch (SQLException sqlE){
            LOGGER.error("checkDepartmenName: {}", sqlE);
            throw new DepartmentFaultService("Inable to connect to databese");
        }
    }

    public void modifyDepartmentName(Integer depId, String name) throws DepartmentFaultService {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(MODIFY_DEPARTMENT_NAME_QUERY);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, depId);
            preparedStatement.executeUpdate();
        }
        catch (SQLException sqlE){
            LOGGER.error("modify department name: {}", sqlE);
            throw new DepartmentFaultService("Inable to connect to databese");
        }
    }

    public void createNewDepartment(String depName, Integer creater) throws DepartmentFaultService {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(ADD_NEW_DEPARTMENT_QUERY);
            preparedStatement.setString(1, depName);
            preparedStatement.setInt(2, creater);
            preparedStatement.executeUpdate();
        }
        catch (SQLException sqlE){
            LOGGER.error("checkDepartmenName: {}", sqlE);
            throw new DepartmentFaultService("Inable to connect to databese");
        }
    }
}