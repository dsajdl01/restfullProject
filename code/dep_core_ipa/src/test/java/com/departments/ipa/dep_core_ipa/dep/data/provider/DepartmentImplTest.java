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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by david on 19/08/16.
 */
public class DepartmentImplTest {

    private Connection dbTestCon;
    private DepartmentImpl depImpl;
    private static HeplerDBO heplerDBO;
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentImplTest.class);

    @BeforeClass
    public static void setUpClass() throws Exception {
        try {
            heplerDBO = new HeplerDBO();
            heplerDBO.getDbTestConnection();
            heplerDBO.addDepartmentValuesToTable(Arrays.asList(new DepartmentTable(null, "Department Team", 1), new DepartmentTable(null, "Finance", 2)));
            heplerDBO.addStaffValuesToTable(new DepartmentImplTest().getStaffData());
        } catch (SQLException e){
            LOGGER.error("SQL DepartmentImplTest: erro= {}", e);
        }
    }

    @Before
    public void setUp() {
            depImpl = new DepartmentImpl(new DepartmentDBO(new DepartmentDBOConnection(heplerDBO.getPropertiesTestDataConfig()).getDbConnection()));
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
    public void getDepartmentTest() throws DepartmentFaultService {
        List<Department> departmentList = depImpl.getDepartmentList();
        final Integer id = departmentList.get(0).getId();
        final String name = departmentList.get(0).getName();
        String creater = departmentList.get(0).getCreater();
        Department department = depImpl.getDepartment(id);

        assertThat(department.getId(), is(id));
        assertThat(department.getName(), is(name));
        assertThat(department.getCreater(), is(creater));
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

    @Test
    public void checkDepartmenNameExistTest() throws DepartmentFaultService {
        assertThat(depImpl.checkDepartmenName("Department Team"), is(true));
    }

    @Test
    public void checkDepartmenNameNotExistTest() throws DepartmentFaultService {
        assertThat(depImpl.checkDepartmenName("Unknow Department"), is(false));
    }

    @Test
    public void createNewDepartmentTest() throws DepartmentFaultService {
        depImpl.createNewDepartment("new department", 2);
        assertThat(depImpl.checkDepartmenName("new department"), is(true));
    }

    @Test
    public void modifyDepartmentNameTest() throws DepartmentFaultService {
        // create a new  departemnt
        depImpl.createNewDepartment("add department", 2);
        assertThat(depImpl.checkDepartmenName("add department"), is(true));
        // modify a new department name
        depImpl.modifyDepartmentName(getDepartmentId("add department"), "old department");
        // checking if department name is renamed
        assertThat(depImpl.checkDepartmenName("add department"), is(false));
        assertThat(depImpl.checkDepartmenName("old department"), is(true));
    }

    private Integer getDepartmentId(String depName) throws DepartmentFaultService{
        List<Department> departmentList = depImpl.getDepartmentList();
        for(Department dep : departmentList){
            if(dep.getName().equals(depName)){
                return dep.getId();
            }
        }
        LOGGER.info("ERROR: getDepartmentId() {} id has not been found!", depName);
        return -1;
    }

    protected List<StaffTable> getStaffData() {
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
