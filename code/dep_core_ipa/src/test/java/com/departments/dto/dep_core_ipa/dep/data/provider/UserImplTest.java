package com.departments.dto.dep_core_ipa.dep.data.provider;

import com.department.core.data.PasswordAuthentication;
import com.departments.dto.common.lgb.CommonConversions;
import com.departments.dto.common.lgb.SearchType;
import com.departments.dto.data.DepartmentTable;
import com.departments.dto.data.LoginDetails;
import com.departments.dto.data.LoginStaff;
import com.departments.dto.data.Staff;
import com.departments.dto.dep_core_ipa.com.provider.helper.HeplerDBO;
import com.departments.dto.dep_dbo.DepartmentDBOConnection;
import com.departments.dto.dep_dbo.UserDBO;
import com.departments.dto.fault.exception.ValidationException;
import com.httpSession.core.HttpSessionCoreServlet;
import dep.data.core.provider.UserImpl;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Created by david on 20/11/16.
 */
public class UserImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserImplTest.class);
    private static HeplerDBO heplerDBO;
    private CommonConversions commonConv = new CommonConversions();
    private UserImpl userImp;
    private static List<Integer>  testedUsersId = new ArrayList<>();
    private static HttpSessionCoreServlet httpSessionCoreServlet = new HttpSessionCoreServlet();
    private static PasswordAuthentication passwordAuth = new PasswordAuthentication( httpSessionCoreServlet );

    @BeforeClass
    public static void setUpClass() throws Exception {
        try {
            heplerDBO = new HeplerDBO();
            heplerDBO.getDbTestConnection();
            heplerDBO.addDepartmentValuesToTable(Arrays.asList(new DepartmentTable(null, "Department Team", 1), new DepartmentTable(null, "Finance", 2)));
            heplerDBO.addStaffValuesToTable(new DepartmentImplTest().getStaffData());
            heplerDBO.addLoginStaff(2, "jolita@diez.com", passwordAuth.hashPassword("password"));
        } catch (SQLException e) {
            LOGGER.error("SQL DepartmentImplTest: error= {}", e.getMessage());
            throw new Exception(e);
        }
    }

    @Before
    public  void setUp() throws Exception {
        userImp = new UserImpl(new UserDBO(new DepartmentDBOConnection(heplerDBO.getPropertiesTestDataConfig()).getDbConnection()), passwordAuth, httpSessionCoreServlet);
        if(!testedUsersId.isEmpty()) {
            heplerDBO.deleteStaffDetails(testedUsersId);
        }
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
        try {
            HttpSession session = mock(HttpSession.class);
            assertThat(userImp.logInUser("some@email.com", "l2j3j23j", session), is(nullValue()));
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Invalid email or password"));
        }
    }

    @Test
    public void logInUserTest() throws Exception {
        HttpSession session = mock(HttpSession.class);
        LoginStaff staffDetails = userImp.logInUser("jolita@diez.com", "password", session);
        assertThat(staffDetails.getName(), is("Jolita Diez"));
        assertThat(staffDetails.getFirstLogin(), is(true));
    }

    @Test
    public void doesEmailExistTest() throws Exception {
        assertThat(userImp.doesEmailExist("jolita@diez.com"), is(true));
        assertThat(userImp.doesEmailExist("jolitaka@co.uk"), is(false));
    }

    @Test
    public void saveNewStaffTest() throws Exception  {
        userImp.saveNewStaff(generateStaff("New Name", "2000-01-01", "2016-06-06", "Sales"));
        final int staffId = userImp.getNewStaffId();
        Staff staffDetails = userImp.getStaffDetails(staffId);

        assertThat(staffDetails.getName(), is("New Name"));
        assertThat(staffDetails.getPosition(), is("Sales"));
        assertThat(commonConv.convertDateToString(staffDetails.getDob()), is("2000-01-01"));
        assertThat(commonConv.convertDateToString(staffDetails.getStartDay()), is("2016-06-06"));

        testedUsersId.add(staffDetails.getId());
    }

    @Test
    public void saveLoginDetailsTest() throws Exception {
        userImp.saveNewStaff(generateStaff("New Full Name", "2000-02-02", "2016-07-07", "Assistant"));
        final int staffId = userImp.getNewStaffId();
        LoginDetails loginDetails = new LoginDetails("newfullname@email.com", passwordAuth.hashPassword("somePassword"));
        userImp.saveLoginDetails(loginDetails, staffId);

        HttpSession session = mock(HttpSession.class);
        LoginStaff staffDetails = userImp.logInUser("newfullname@email.com", "somePassword", session);
        assertThat(staffDetails.getName(), is("New Full Name"));
        assertThat(staffDetails.getUserId(), is(staffId));

        testedUsersId.add(staffDetails.getUserId());
    }

    @Test
    public void saveNewStaffAndLoginDetailsTest() throws Exception {
        Staff staff = generateStaff("Bob Marley", "2000-10-12", "2016-07-11", "Java Developer");
        LoginDetails loginDetails = new LoginDetails("bob@marley.com", passwordAuth.hashPassword("someBobPassword"));
        userImp.saveNewStaffAndLoginDetails(staff, loginDetails);
        final int staffId = userImp.getNewStaffId();

        Staff staffDetails = userImp.getStaffDetails(staffId);

        assertThat(staffDetails.getName(), is("Bob Marley"));
        assertThat(staffDetails.getPosition(), is("Java Developer"));
        assertThat(commonConv.convertDateToString(staffDetails.getDob()), is("2000-10-12"));
        assertThat(commonConv.convertDateToString(staffDetails.getStartDay()), is("2016-07-11"));

        HttpSession session = mock(HttpSession.class);
        LoginStaff loginStaffDetails = userImp.logInUser("bob@marley.com", "someBobPassword", session);
        assertThat(loginStaffDetails.getName(), is("Bob Marley"));
        assertThat(loginStaffDetails.getUserId(), is(staffId));

        testedUsersId.add(staffDetails.getId());
    }

    @Test
    public void deleteStaffTest() throws Exception {
        Staff staff = generateStaff("John Smith", "2000-03-03", "2016-03-03", "Manager");
        LoginDetails loginDetails = new LoginDetails("john@smith.com", passwordAuth.hashPassword("someJohnPassword!123"));
        userImp.saveNewStaffAndLoginDetails(staff, loginDetails);

        final int staffId = userImp.getNewStaffId();
        Staff staffDetails = userImp.getStaffDetails(staffId);

        assertThat(staffDetails.getId(), is(staffId));
        assertThat(staffDetails.getName(), is("John Smith"));
        assertThat(staffDetails.getPosition(), is("Manager"));
        assertThat(commonConv.convertDateToString(staffDetails.getDob()), is("2000-03-03"));
        assertThat(commonConv.convertDateToString(staffDetails.getStartDay()), is("2016-03-03"));

        HttpSession session = mock(HttpSession.class);
        LoginStaff loginStaffDetails = userImp.logInUser("john@smith.com", "someJohnPassword!123", session);
        assertThat(loginStaffDetails.getName(), is("John Smith"));
        assertThat(loginStaffDetails.getUserId(), is(staffId));

        // delete staff
        userImp.deleteStaff(staffId);
        assertThat(userImp.getStaffDetails(staffId), is(nullValue()));
        try {
            loginStaffDetails = userImp.logInUser("john@smith.com", "someJohnPassword!123", session);
            assertThat(loginStaffDetails, is(nullValue()));
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Invalid email or password"));
        }
    }

    @Test
    public void searchForStaffsByName_Test() throws Exception {
        // Added new staff include name Diez
        userImp.saveNewStaff(generateStaff(2, "Monserat Diez", "1971-02-01", "2016-01-01", "Designer"));

        // search for name Diez
        List<Staff> staffs = userImp.searchForStaffs(2, "Diez", SearchType.NAME);

        assertThat(staffs.size(), is(2));
        for (Staff s : staffs) {
            if (s.getName().equalsIgnoreCase("Monserat Diez")){
                assertThat(s.getPosition(), is("Designer"));
                testedUsersId.add(s.getId());
            } else {
                assertThat(s.getName(), is("Jolita Diez"));
                assertThat(s.getPosition(), is("Economist"));
            }
        }
    }

    @Test
    public void searchForStaffsByDBO_Test() throws Exception  {
        userImp.saveNewStaff(generateStaff("Bob Dylan", "1977-09-08", "2016-01-01", "Java Developer"));

        List<Staff> staffs = userImp.searchForStaffs(1, "1977-09-08", SearchType.DOB);
        assertThat(staffs.size(), is(1));
        Staff staff = staffs.get(0);
        assertThat(staff.getName(), is("Bob Dylan"));
        assertThat(staff.getPosition(), is("Java Developer"));
        testedUsersId.add(staff.getId());
    }

    @Test
    public void searchForStaffsByDBOError_Test() throws Exception  {
        try {
            userImp.searchForStaffs(1, "1977-09-38", SearchType.DOB);
            fail( "searchForStaffsByDBOError_Test: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Invalid date of birthday: 1977-09-38"));
        }


    }

    @Test
    public void checkIfStaffExist_Test() throws Exception {
        assertThat(userImp.checkIfStaffExist(userImp.getNewStaffId()), is(notNullValue()));
        assertThat(userImp.checkIfStaffExist(11), is(nullValue()));


    }

    @Test
    public void staffToModify_Test() throws Exception {
        userImp.saveNewStaff(generateStaff("Anna Kendrick", "1985-08-09", "2016-10-14", "Java Developer"));
        final int AnnaId = userImp.getNewStaffId();
        final Staff staff = userImp.checkIfStaffExist(AnnaId);
        assertThat(staff.getName(), is("Anna Kendrick"));
        assertThat(staff.getPosition(), is("Java Developer"));
        assertThat(staff.getEmail(), is("some@email.com"));
        assertThat(staff.getComment(), is("some text"));

        //generate new Anna object
        Staff modifyAnnaObj = new Staff.Builder().setId(AnnaId).setDepId(1).setName("Anna Kendrick").setDob(commonConv.getDateFromString("1985-08-09")).setStartDay(commonConv.getDateFromString("2016-10-14"))
                .setPosition("Entertainment").setEmail("anna@kendrick.com").setComment("great singer").build();
        userImp.modifyStaffDetails(modifyAnnaObj);
        final Staff annaWithNewDetails = userImp.checkIfStaffExist(AnnaId);
        assertThat(annaWithNewDetails.getName(), is("Anna Kendrick"));
        assertThat(annaWithNewDetails.getPosition(), is("Entertainment"));
        assertThat(annaWithNewDetails.getEmail(), is("anna@kendrick.com"));
        assertThat(annaWithNewDetails.getComment(), is("great singer"));

        testedUsersId.add(AnnaId);
    }

    @Test
    public void staffToModifyDepIdDoesNotMath_Test() throws Exception {
        userImp.saveNewStaff(generateStaff("New Anna Name", "1985-08-09", "2016-10-14", "Software Developer"));
        final int AnnaId = userImp.getNewStaffId();
        final Staff staff = userImp.checkIfStaffExist(AnnaId);
        assertThat(staff.getName(), is("New Anna Name"));
        assertThat(staff.getPosition(), is("Software Developer"));
        assertThat(staff.getEmail(), is("some@email.com"));
        assertThat(staff.getComment(), is("some text"));

        //generate new Anna object
        try {
            Staff modifyAnnaObj = new Staff.Builder().setId(AnnaId).setDepId(4).setName("Anna Kendrick").setDob(commonConv.getDateFromString("1985-08-09")).setStartDay(commonConv.getDateFromString("2016-10-14"))
                    .setPosition("Entertainment").setEmail("anna@kendrick.com").setComment("great singer").build();
            userImp.modifyStaffDetails(modifyAnnaObj);
            fail( "staffToModifyDepIdDoesNotMath_Test: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Department id does not match."));
        }
        testedUsersId.add(AnnaId);
    }

    @Test
    public void staffToModifyStaffIdDoesNotExist_Test() throws Exception {
        userImp.saveNewStaff(generateStaff("Anna Kendrick", "1985-08-09", "2016-10-14", "Software Developer"));
        final int AnnaId = userImp.getNewStaffId();
        final Staff staff = userImp.checkIfStaffExist(AnnaId);
        assertThat(staff.getName(), is("Anna Kendrick"));
        assertThat(staff.getPosition(), is("Software Developer"));
        assertThat(staff.getEmail(), is("some@email.com"));
        assertThat(staff.getComment(), is("some text"));

        //generate new Anna object
        try {
            Staff modifyAnnaObj = new Staff.Builder().setId(31).setDepId(1).setName("Anna Kendrick").setDob(commonConv.getDateFromString("1985-08-09")).setStartDay(commonConv.getDateFromString("2016-10-14"))
                    .setPosition("Entertainment").setEmail("anna@kendrick.com").setComment("great singer").build();
            userImp.modifyStaffDetails(modifyAnnaObj);
            fail( "staffToModifyStaffIdDoesNotExist_Test: ValidationException should be thrown here. ");
        } catch (ValidationException e) {
            assertThat(e.getMessage(), is("Staff id 31 does not exist for staff name Anna Kendrick"));
        }
        testedUsersId.add(AnnaId);
    }

    private Staff generateStaff(String name, String dob, String startDate, String position) throws Exception {
        return generateStaff(1, name, dob, startDate, position);
    }

    private Staff generateStaff(int depId, String name, String dob, String startDate, String position) throws Exception {
        Staff.Builder staff = new Staff.Builder();
        staff.setDepId(depId);
        staff.setName(name);
        staff.setDob(commonConv.getDateFromString(dob));
        staff.setStartDay(commonConv.getDateFromString(startDate));
        staff.setPosition(position);
        staff.setEmail("some@email.com");
        staff.setComment("some text");
        return staff.build();
    }
}
