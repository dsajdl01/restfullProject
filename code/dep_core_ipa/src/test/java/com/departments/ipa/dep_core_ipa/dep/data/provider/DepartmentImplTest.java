package com.departments.ipa.dep_core_ipa.dep.data.provider;
import com.departments.ipa.dep_core_ipa.com.provider.helper.HeplerDBO;

import org.junit.Before;
import org.junit.Test;
import org.junit.AfterClass;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by david on 19/08/16.
 */
public class DepartmentImplTest {

    private static Connection dbTestCon;

    @Before
    public void setUp()
    {
        dbTestCon = new HeplerDBO().getDbTestConnection();
        addDepartmentValueToBDOTestTable();
        System.out.println("\nsetup() method ===========\n");
    }
    @AfterClass
    public static void afterClass(){
        truncateTable();

    }


    @Test
    public void getDepartmentListTest(){

        System.out.println("\ngetDepartmentList() method ===========\n");
    }

    private static void truncateTable(){
        String sqlTruncateTable = "truncate table department";
        try {
            PreparedStatement preparedStatement = dbTestCon.prepareStatement(sqlTruncateTable);
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addDepartmentValueToBDOTestTable(){
        String sqlQueruAddDepartmentVal =  "INSERT INTO department(name, createdBy) VALUES(?,?)";
        try {

            PreparedStatement preparedStatement = dbTestCon.prepareStatement(sqlQueruAddDepartmentVal);
            preparedStatement.setString(1, "Department Team");
            preparedStatement.setInt(2, 1);
            preparedStatement.addBatch();
            preparedStatement.setString(1, "Finance");
            preparedStatement.setInt(2, 1);
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
