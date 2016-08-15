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

    protected static final Logger LOGGER = LoggerFactory.getLogger(DepartmentDBO.class);
    private static final String ALL_DEPARTMENT_QUERY = "select id, name, createdBy from department where id !=1";
    private static final String CREATER_NAME_QUERY = "select name from staff where id = 1";

    public List<DepartmentDbTable> getDepartments(Statement con) {
        List<DepartmentDbTable> departmentListTables = new ArrayList<DepartmentDbTable>();
        try {
            ResultSet resSet = con.executeQuery(ALL_DEPARTMENT_QUERY);
            String result = "";
            while(resSet.next()){
                LOGGER.error("id:{}, name={}, createdBy={} ", resSet.getString("id"),  resSet.getString("name"),  resSet.getString("createdBy"));
                departmentListTables.add(new DepartmentDbTable(resSet.getInt("id"), resSet.getString("name"), resSet.getInt("createdBy")));
            }
            return  departmentListTables;
        } catch (SQLException sqlE){
            LOGGER.error("getDepartments: {}", sqlE);
        }
        return null;
    }

    public String getCreaterName(Integer createrId, Statement con){
        try {
            ResultSet resSet = con.executeQuery(CREATER_NAME_QUERY);
            resSet.next();
            return resSet.getString("name");
        } catch (SQLException sqlE){
            LOGGER.error("getCreaterName: {}", sqlE);
            return null;
        }
    }

}