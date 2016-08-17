package com.departments.ipa.dep_dbo;
//        com.departments/ipa/dep_dbo/DepartmentDBO.java

import com.departments.ipa.data.DepartmentDbTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
/**
 * Hello world!
 *
 */
public class DepartmentDBO  {

    private Connection con;

    protected static final Logger LOGGER = LoggerFactory.getLogger(DepartmentDBO.class);
    private static final String ALL_DEPARTMENT_QUERY = "select id, name, createdBy from department where id !=1";
    private static final String CREATER_NAME_QUERY = "select name from staff where id = ?";
    public DepartmentDBO(Connection con){
        this.con = con;
    }

    public List<DepartmentDbTable> getDepartments() {
        List<DepartmentDbTable> departmentListTables = new ArrayList<DepartmentDbTable>();
        PreparedStatement preparedStatement = null;
        try {

            preparedStatement = con.prepareStatement(ALL_DEPARTMENT_QUERY);
            ResultSet resSet = preparedStatement.executeQuery();
            while(resSet.next()){
                departmentListTables.add(new DepartmentDbTable(resSet.getInt("id"), resSet.getString("name"), resSet.getInt("createdBy")));
            }
            return  departmentListTables;
        } catch (SQLException sqlE){
            LOGGER.error("getDepartments: {}", sqlE);
        }
        return null; // throw exception later
    }

    public String getCreaterName(Integer createrId){
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(CREATER_NAME_QUERY);
            preparedStatement.setInt(1, createrId);
            ResultSet resSet = preparedStatement.executeQuery();
            if(resSet != null) {
                resSet.next();
                return  resSet.getString(1);
            }
        } catch (SQLException sqlE){
            LOGGER.error("getCreaterName: {}", sqlE);
        }
        return null; // throw exception later
    }

}