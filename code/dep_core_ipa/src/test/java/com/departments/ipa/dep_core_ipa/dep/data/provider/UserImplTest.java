package com.departments.ipa.dep_core_ipa.dep.data.provider;

import com.departments.ipa.data.DepartmentTable;
import com.departments.ipa.data.LoginStaff;
import com.departments.ipa.dep_core_ipa.com.provider.helper.HeplerDBO;
import com.departments.ipa.dep_dbo.DepartmentDBOConnection;
import com.departments.ipa.dep_dbo.UserDBO;
import dep.data.provider.UserImpl;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Arrays;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
/**
 * Created by david on 20/11/16.
 */
public class UserImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserImplTest.class);
    private static HeplerDBO heplerDBO;
    private UserImpl userImp;

    @BeforeClass
    public static void setUpClass() throws Exception {
        try {
            heplerDBO = new HeplerDBO();
            heplerDBO.getDbTestConnection();
            heplerDBO.addDepartmentValuesToTable(Arrays.asList(new DepartmentTable(null, "Department Team", 1), new DepartmentTable(null, "Finance", 2)));
            heplerDBO.addStaffValuesToTable(new DepartmentImplTest().getStaffData());
            heplerDBO.addLoginStaff(2, "jolita@diez.com", "password");
        } catch (SQLException e) {
            LOGGER.error("SQL DepartmentImplTest: error= {}", e.getMessage());
            throw new Exception(e);
        }
    }

    @Before
    public  void setUp() {
        userImp = new UserImpl(new UserDBO(new DepartmentDBOConnection(heplerDBO.getPropertiesTestDataConfig()).getDbConnection()));
    }

    @AfterClass
    public static void afterClass() throws Exception {
        try {
            heplerDBO.truncateTable();
        } catch (SQLException e) {
            LOGGER.error("SQL TRUNCATE error: {}", e);
        }
    }

    @Test
    public void logInUserWithIncorrectValueTest() throws Exception {
        assertThat(userImp.logInUser("some@email.com", "l2j3j23j"), is(nullValue()));
    }

    @Test
    public void logInUserTest() throws Exception {
        LoginStaff staffDetails = userImp.logInUser("jolita@diez.com", "password");
        assertThat(staffDetails.getName(), is("Jolita Diez"));
    }
    @Test
    public void doesEmailExistTest() throws Exception {
        assertThat(userImp.doesEmailExist("jolita@diez.com"), is(true));
        assertThat(userImp.doesEmailExist("jolitaka@co.uk"), is(false));
    }
}
