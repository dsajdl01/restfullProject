package com.departments.ipa.dep_core_ipa.dep.data.provider;

import com.departments.ipa.data.Department;
import com.departments.ipa.data.DepartmentTable;
import com.departments.ipa.data.StaffTable;
import com.departments.ipa.dep_core_ipa.com.provider.helper.HeplerDBO;
import com.departments.ipa.dep_dbo.DepartmentDBO;
import com.departments.ipa.dep_dbo.DepartmentDBOConnection;
import dep.data.provider.DepartmentImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.AfterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by david on 19/08/16.
 */
public class DepartmentImplTest {

    private Connection dbTestCon;
    private DepartmentImpl depImpl;
    private static HeplerDBO heplerDBO;
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentImplTest.class);

    @Before
    public void setUp()
    {
        try {
            heplerDBO = new HeplerDBO();
            dbTestCon = heplerDBO.getDbTestConnection();
            heplerDBO.addDepartmentValuesToTable(Arrays.asList(new DepartmentTable(null, "Department Team", 1), new DepartmentTable(null, "Finance", 2)));
            heplerDBO.addStaffValuesToTable(getStaffData());
            depImpl = new DepartmentImpl(new DepartmentDBO(new DepartmentDBOConnection(heplerDBO.getPropertiesTestDataConfig()).getDbConnection()));
        } catch (SQLException e){
            LOGGER.error("SQL error: {}", e);
        }
    }
    @AfterClass
    public static void afterClass(){
        try {
            heplerDBO.truncateTable();
        } catch (SQLException e){
            LOGGER.error("SQL TRUNCATE error: {}", e);
        }
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

    private List<StaffTable> getStaffData() {
        List<StaffTable> staff = new ArrayList<StaffTable>();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dob = dateFormat.parse("2013-12-04");
            Date startDate = dateFormat.parse("2009-11-24");
            staff.add(new StaffTable(null, 1, "David Sajdl", dob, startDate, null, "Developer", "ds@example.co.uk", null));
            dob = dateFormat.parse("1984-05-17");
            startDate = dateFormat.parse("2004-01-02");
            staff.add(new StaffTable(null, 2, "Jolita Diez", dob, startDate, null, "Economist", "jd@example.com", "Exelent communication"));
            return staff;
        } catch (ParseException e){
            LOGGER.error("Date convertion error: {}", e);
        }
        return null;
    }
}
