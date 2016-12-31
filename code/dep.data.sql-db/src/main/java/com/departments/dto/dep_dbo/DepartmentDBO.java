package com.departments.dto.dep_dbo;
//        com.departments/dto/dep_dbo/DepartmentDBO.java

import com.departments.dto.data.Department;
import com.departments.dto.dep_dbo_interface.DepartmentDBOInter;
import com.departments.dto.fault.exception.SQLFaultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class DepartmentDBO implements DepartmentDBOInter {

    private Connection con;

    protected static final Logger LOGGER = LoggerFactory.getLogger(DepartmentDBO.class);
    private static  final String selectDepartmetsQuery = "select dep.id as id, dep.name as name, " +
            "staff.name as creater from department dep, staff staff where dep.createdBy = staff.dep_id";

    private static final String ALL_DEPARTMENT_QUERY = selectDepartmetsQuery +" and dep.name != 'root'";
    private static final String CHECK_DEP_NAME_QUARY = "select count(name) as numberOfDepNames from department where name like ?";
    private static final String ADD_NEW_DEPARTMENT_QUERY = "insert into department (name, createdBy) values (?, ?);";
    private static final String MODIFY_DEPARTMENT_NAME_QUERY = "UPDATE department SET name= ? where id = ?";
    private static final String GET_DEPARTMENT_BY_ID =  selectDepartmetsQuery + " AND dep.id = ?";
    private static final String CHECK_DEPID_EXIST_QUERY = "SELECT count(id) AS numOfDep FROM department WHERE id = ?";

    public DepartmentDBO(Connection con){
        this.con = con;
    }

    public List<Department> getDepartments() throws SQLFaultException {
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
            throw new SQLFaultException("Inable to connect to database");
        }
    }

    public boolean checkDepartmenName(String depName) throws SQLFaultException {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(CHECK_DEP_NAME_QUARY);
            preparedStatement.setString(1, depName);
            ResultSet resSet = preparedStatement.executeQuery();
            resSet.next();
            return resSet.getInt("numberOfDepNames") == 0 ? false : true;
        }
            catch (SQLException sqlE){
            LOGGER.error("checkDepartmenName: {}", sqlE);
            throw new SQLFaultException("Inable to connect to database");
        }
    }

    public void modifyDepartmentName(Integer depId, String name) throws SQLFaultException {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(MODIFY_DEPARTMENT_NAME_QUERY);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, depId);
            preparedStatement.executeUpdate();
        }
        catch (SQLException sqlE){
            LOGGER.error("modify department name: {}", sqlE);
            throw new SQLFaultException("Inable to connect to database");
        }
    }

    public boolean doesDepartmentExist(int depId ) throws SQLFaultException {
        LOGGER.info("doesDepartmentExist: depId={}", depId);
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(CHECK_DEPID_EXIST_QUERY);
            preparedStatement.setInt(1, depId);
            ResultSet resSet = preparedStatement.executeQuery();
            resSet.next();
            return resSet.getInt("numOfDep") == 0 ? false : true;
        } catch (SQLException sqlE) {
            LOGGER.error("loninUserId: {}", sqlE);
            throw new SQLFaultException("Enable to connect to database");
        }
    }

    public Department getDepartment(Integer depId) throws SQLFaultException {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(GET_DEPARTMENT_BY_ID);
            preparedStatement.setInt(1, depId);

            ResultSet resSet = preparedStatement.executeQuery();
            resSet.next();
            return new Department(resSet.getInt("id"), resSet.getString("name"), resSet.getString("creater"));
        }catch (SQLException sqlE){
            LOGGER.error("getDepartment: {}", sqlE);
            throw new SQLFaultException("Inable to connect to database");
        }
    }
    public void createNewDepartment(String depName, Integer creater) throws SQLFaultException {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(ADD_NEW_DEPARTMENT_QUERY);
            preparedStatement.setString(1, depName);
            preparedStatement.setInt(2, creater);
            preparedStatement.executeUpdate();
        }
        catch (SQLException sqlE){
            LOGGER.error("checkDepartmenName: {}", sqlE);
            throw new SQLFaultException("Inable to connect to database");
        }
    }
}