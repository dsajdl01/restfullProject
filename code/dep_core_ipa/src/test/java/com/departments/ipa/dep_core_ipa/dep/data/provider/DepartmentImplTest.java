package com.departments.ipa.dep_core_ipa.dep.data.provider;
import com.departments.ipa.data.Department;
import com.departments.ipa.dep_core_ipa.com.provider.helper.HeplerDBO;

import com.departments.ipa.dep_dbo.DepartmentDBO;
import com.departments.ipa.dep_dbo.DepartmentDBOConnection;
import dep.data.provider.DepartmentImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.AfterClass;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by david on 19/08/16.
 */
public class DepartmentImplTest {

    private static Connection dbTestCon;
    private DepartmentImpl depImpl;

    @Before
    public void setUp()
    {
        HeplerDBO heplerDBO = new HeplerDBO();
        dbTestCon = heplerDBO.getDbTestConnection();
        depImpl = new DepartmentImpl(new DepartmentDBO( new DepartmentDBOConnection(heplerDBO.getPropertiesTestDataConfig()).getDbConnection()));
        addValueToBDOTestTable();
    }
    @AfterClass
    public static void afterClass(){
        truncateTable();

    }


    @Test
    public void getDepartmentListTest(){

        List<Department> departmentList = depImpl.getDepartmentList();
        assertThat(departmentList.size(), is(2));
        int count = 1;
        for (Department dep : departmentList) {
            if( count == 1 ){
                assertThat(dep.getName(), is("Department Team"));
                assertThat(dep.getCreater(), is("David Sajdl"));
                count++;
            } else {
                assertThat(dep.getName(), is("Finance"));
                assertThat(dep.getCreater(), is("Jolita Diez"));
            }
        }
    }

    private static void truncateTable(){
        String sqlTruncateDepartmentTable = "truncate table department";
        String sqlTruncateStaffTable = "truncate table staff";
        String sqlRemoveConstraint = "ALTER TABLE staff DROP FOREIGN KEY db_04E4BC85_staff_id";
        String sqlAddConstraint = "ALTER TABLE staff ADD CONSTRAINT db_04E4BC85_staff_id FOREIGN KEY (dep_id) REFERENCES department (id)";
        try {
            PreparedStatement preparedStatement = dbTestCon.prepareStatement(sqlRemoveConstraint);
            preparedStatement.executeUpdate();

            preparedStatement = dbTestCon.prepareStatement(sqlTruncateStaffTable);
            preparedStatement.executeUpdate();

            preparedStatement = dbTestCon.prepareStatement(sqlTruncateDepartmentTable);
            preparedStatement.executeUpdate();

            preparedStatement = dbTestCon.prepareStatement(sqlAddConstraint);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addValueToBDOTestTable(){
        String sqlQueruAddDepartmentVal =  "INSERT INTO department(name, createdBy) VALUES(?,?)";
        String SqlQueryAssStaff = "INSERT INTO staff (dep_id, name, dob, start_day, last_day, position, email, comments) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {

            addDepartmentValues(sqlQueruAddDepartmentVal);
            PreparedStatement preparedStatement;

            preparedStatement = dbTestCon.prepareStatement(SqlQueryAssStaff);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "David Sajdl");
            preparedStatement.setString(3, "1980-02-25");
            preparedStatement.setString(4, "2009-11-24");
            preparedStatement.setString(5, null);
            preparedStatement.setString(6, "Developer");
            preparedStatement.setString(7, "ds@example.co.uk");
            preparedStatement.setString(8, null);
            preparedStatement.addBatch();

            preparedStatement.setInt(1, 2);
            preparedStatement.setString(2, "Jolita Diez");
            preparedStatement.setString(3, "1984-05-17");
            preparedStatement.setString(4, "2004-01-02");
            preparedStatement.setString(5, null);
            preparedStatement.setString(6, "Economist");
            preparedStatement.setString(7, "jd@example.com");
            preparedStatement.setString(8, "Exelent communication");

            preparedStatement.addBatch();
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addDepartmentValues(String sqlQueruAddDepartmentVal) throws SQLException {
        PreparedStatement preparedStatement = dbTestCon.prepareStatement(sqlQueruAddDepartmentVal);
        preparedStatement.setString(1, "Department Team");
        preparedStatement.setInt(2, 1);
        preparedStatement.addBatch();
        preparedStatement.setString(1, "Finance");
        preparedStatement.setInt(2, 2);
        preparedStatement.addBatch();
        preparedStatement.executeBatch();
    }
}
