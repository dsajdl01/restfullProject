package com.departments.ipa.dep_core_ipa.dep.data.provider;

import com.departments.ipa.common.lgb.CommonConversions;
import com.departments.ipa.data.Department;
import com.departments.ipa.data.DepartmentTable;
import com.departments.ipa.data.StaffTable;
import com.departments.ipa.dep_core_ipa.com.provider.helper.HeplerDBO;
import com.departments.ipa.dep_dbo.DepartmentDBO;
import com.departments.ipa.dep_dbo.DepartmentDBOConnection;
import com.departments.ipa.fault.exception.DepartmentFaultService;
import dep.data.provider.DepartmentImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.AfterClass;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
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
    public void getDepartmentListTest() throws DepartmentFaultService {

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
        CommonConversions convertion = new CommonConversions();
        try {
            staff.add(new StaffTable(null, 1, "David Sajdl", convertion.getDateFromString("2009-11-24"), convertion.getDateFromString("2009-11-24"), null, "Developer", "ds@example.co.uk", null));
            staff.add(new StaffTable(null, 2, "Jolita Diez", convertion.getDateFromString("1984-05-17"), convertion.getDateFromString("2004-01-02"), null, "Economist", "jd@example.com", "Exelent communication"));
            return staff;
        } catch (ParseException e){
            LOGGER.error("DepartmentImplTest class, Date convertion error: {}", e);
        }
        return null;
    }
}
