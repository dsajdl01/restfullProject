package com.controlcenter.homerestipa;

import com.controlcenter.homerestipa.provider.RestServices;
import com.controlcenter.homerestipa.response.StaffLoginDetailsJson;
import com.controlcenter.homerestipa.response.UserLoginJson;
import com.department.testutils.JerseyContainerJUnitRule;
import com.departments.dto.common.lgb.CommonConversions;
import com.departments.dto.common.lgb.SearchType;
import com.departments.dto.data.LoginDetails;
import com.departments.dto.data.LoginStaff;
import com.departments.dto.data.Staff;
import com.departments.dto.fault.exception.SQLFaultException;
import com.departments.dto.fault.exception.ValidationException;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.jayway.restassured.RestAssured;
import dep.data.core.provider.inter.provider.DepartmentCoreServices;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.List;

import static com.controlcenter.homerestipa.MockServices.*;
import static com.jayway.restassured.RestAssured.given;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by david on 04/12/16.
 */
public class UserIpaTest {

    private CommonConversions commonConv = new CommonConversions();
    @ClassRule
    public static JerseyContainerJUnitRule jerseyProvider = new JerseyContainerJUnitRule() {
        @Override
        public Application configure()
        {
            return new ResourceConfig() {
                {
                    register(new CCBinder()).
                            register(JacksonJsonProvider.class).
                            register(MultiPartFeature.class).
                            packages(true, "com.controlcenter.homerestipa").
                            addProperties(new HashMap<String, Object>() {{
                                put("com.sun.jersey.api.json.POJOMappingFeature", Boolean.TRUE);
                                put("jersey.config.server.tracing", "ALL");
                                put("jersey.config.server.tracing.threshold", "VERBOSE");
                            }});
                }

                class CCBinder extends AbstractBinder {
                    @Override
                    protected void configure() {
                        bind(mockDepartmentCoreServices).to(DepartmentCoreServices.class);
                        bind(mockRestServices).to(RestServices.class);
                        bindFactory(new HttpServletRequestFactory()).to(HttpServletRequest.class);
                    }
                }

                // resource: http://stackoverflow.com/questions/28670665/how-to-write-unit-test-for-this-class-using-jersey-2-test-framework
                class HttpServletRequestFactory implements Factory<HttpServletRequest> {

                    @Override
                    public HttpServletRequest provide() {
                        return mockHttpServletRequest;
                    }

                    @Override
                    public void dispose(HttpServletRequest t) {}
                }
            };
        }
    };

    @Before
    public void before() {
        // RestAssured.baseURI = "http://localhost";   -- This is the default
        // Jersey Test Framework starts Test Container up on port 9998
        RestAssured.port = 9998;
        RestAssured.basePath = "/user";
        MockServices.resetMocks();
    }

    @Test
    public void logInUserSuccess() throws Exception {
        LoginStaff loginStaff = new LoginStaff(1, "Fred Smith", true);
        when(mockUseInter.logInUser("smith@fred.com","password")).thenReturn(loginStaff);
        when(mockHttpServletRequest.getSession(true)).thenReturn(httpSessionMock);
        UserLoginJson userLogin = new UserLoginJson("smith@fred.com","password");

        given()
            .contentType("application/json")
            .body( userLogin )
        .when()//.log().all()
            .put("/login")
        .then()//.log().all()
            .statusCode(HTML_OK)
            .body("userId", equalTo(1))
            .body("name", equalTo("Fred Smith"))
            .body("firstLogin", equalTo(true));

    }

    @Test
    public void logInUserFalseAttempt() throws Exception {

        when(mockUseInter.logInUser("smith@fred.com","passwordsss")).thenReturn(null);
        when(mockHttpServletRequest.getSession(true)).thenReturn(httpSessionMock);
        UserLoginJson userLogin = new UserLoginJson("smith@fred.com","passwordsss");

        given()
            .contentType("application/json")
            .body( userLogin )
        .when()//.log().all()
            .put("/login")
        .then()//.log().all()
            .statusCode(BAD_REQUEST)
            .body("message", equalTo("Invalid email or password"));
    }

    @Test
    public void logInUserWithNullValueTest() throws Exception {

        UserLoginJson userLogin = new UserLoginJson("smith@fred.com",null);

        given()
            .contentType("application/json")
            .body( userLogin )
        .when()//.log().all()
            .put("/login")
        .then()//.log().all()
            .statusCode(BAD_REQUEST)
            .body("message", equalTo("Mandatory argument email or password are missing"));
    }

    @Test
    public void logInUserSQLError() throws Exception {
        LoginStaff loginStaff = new LoginStaff(1, "Fred Smith", true);
        doThrow(new SQLFaultException("Enable to connect to database")).when(mockUseInter).logInUser("smith@fred.com","password");
        UserLoginJson userLogin = new UserLoginJson("smith@fred.com","password");

        given()
            .contentType("application/json")
            .body( userLogin )
        .when()//.log().all()
            .put("/login")
        .then()//.log().all()
            .statusCode(SERVICE_UNAVAILABLE)
            .body("message", equalTo("Enable to connect to database"));
    }

    @Test
    public void logInUserError() throws Exception {
        LoginStaff loginStaff = new LoginStaff(1, "Fred Smith", true);
        doThrow(new RuntimeException()).when(mockUseInter).logInUser("smith@fred.com","password");
        UserLoginJson userLogin = new UserLoginJson("smith@fred.com","password");

        given()
            .contentType("application/json")
            .body( userLogin )
        .when()//.log().all()
            .put("/login")
        .then()//.log().all()
            .statusCode(INTERNAL_SERVER_ERROR);
    }

    @Test
    public void doesEmailExistTest() throws Exception {
        String email = "example@co.uk";
        when(mockUseInter.doesEmailExist(email)).thenReturn(true);
        given()
            .queryParam("email", email)
        .when()//.log().all()
            .get("/emailExist")
        .then()//.log().all()
            .statusCode(HTML_OK)
            .body(equalTo("true"));

    }

    @Test
    public void doesEmailExistBedRequestTest() throws Exception {
        String email = "";
        given()
            .queryParam("email", email)
        .when()//.log().all()
            .get("/emailExist")
        .then()//.log().all()
            .statusCode(BAD_REQUEST)
            .body("message", equalTo("Mandatory argument email is missing"));

    }

    @Test
    public void doesEmailExistError() throws Exception {
        String email = "example@co.uk";
        doThrow(new RuntimeException()).when(mockUseInter).doesEmailExist(email);

        given()
            .queryParam("email", email)
        .when()//.log().all()
            .get("/emailExist")
        .then()//.log().all()
             .statusCode(INTERNAL_SERVER_ERROR);
    }

    @Test
    public void doesEmailExistSQLError() throws Exception {
        String email = "example@co.uk";
        doThrow(new SQLFaultException("Enable to connect to database")).when(mockUseInter).doesEmailExist(email);

        given()
            .queryParam("email", email)
        .when()//.log().all()
             .get("/emailExist")
        .then()//.log().all()
            .statusCode(SERVICE_UNAVAILABLE)
            .body("message", equalTo("Enable to connect to database"));
    }

    @Test
    public void addNewStaffSuccessTest() throws Exception {
        LoginDetails loginDetails = new LoginDetails("some@email.com", "$3728bdkjabddbeqnrrekop");
        Staff staff = new Staff.Builder().build();
        int depId = 2;
        doNothing().when(mockValidationStaffHepler).basicStaffValidation(eq(depId), any(StaffLoginDetailsJson.class));
        when(mockValidationStaffHepler.validateAndGetLoginDetails("some@email.com", "somepassword120")).thenReturn(loginDetails);
        when(mockValidationStaffHepler.validateAndGetStaffDetails(eq(depId), any(StaffLoginDetailsJson.class))).thenReturn(staff);
        doNothing().when(mockUseInter).saveNewStaffAndLoginDetails(staff, loginDetails);

        given()
            .contentType("application/json")
            .body( generateStaffDetails() )
        .when()//.log().all()
            .put("/" + depId + "/addNewStaff")
        .then()//.log().all()
            .statusCode(HTML_OK);
    }

    @Test
    public void addNewStaffValidationErrorTest() throws Exception {
        LoginDetails loginDetails = new LoginDetails("some@email.com", "$3728bdkjabddbeqnrrekop");
        Staff staff = new Staff.Builder().build();
        int depId = 2;
        doThrow(new ValidationException("Invalid Email Address")).when(mockValidationStaffHepler).basicStaffValidation(eq(depId), any(StaffLoginDetailsJson.class));

        given()
            .contentType("application/json")
            .body( generateStaffDetails() )
        .when()//.log().all()
            .put("/" + depId + "/addNewStaff")
        .then()//.log().all()
            .statusCode(BAD_REQUEST)
            .body("message", equalTo("Invalid Email Address"));
    }

    @Test
    public void addNewStaffSQLErrorTest() throws Exception {
        LoginDetails loginDetails = new LoginDetails("some@email.com", "$3728bdkjabddbeqnrrekop");
        Staff staff = new Staff.Builder().build();
        int depId = 2;
        doNothing().when(mockValidationStaffHepler).basicStaffValidation(eq(depId), any(StaffLoginDetailsJson.class));
        when(mockValidationStaffHepler.validateAndGetLoginDetails("some@email.com", "somepassword120")).thenReturn(loginDetails);
        when(mockValidationStaffHepler.validateAndGetStaffDetails(eq(depId), any(StaffLoginDetailsJson.class))).thenReturn(staff);
        doThrow( new SQLFaultException("Enable to connect database")).when(mockUseInter).saveNewStaffAndLoginDetails(staff, loginDetails);

        given()
            .contentType("application/json")
            .body( generateStaffDetails() )
        .when()//.log().all()
            .put("/" + depId + "/addNewStaff")
        .then()//.log().all()
            .statusCode(SERVICE_UNAVAILABLE)
            .body("message", equalTo("Enable to connect database"));
    }

    @Test
    public void addNewStaffFailerTest() throws Exception {
        LoginDetails loginDetails = new LoginDetails("some@email.com", "$3728bdkjabddbeqnrrekop");
        Staff staff = new Staff.Builder().build();
        int depId = 2;
        doNothing().when(mockValidationStaffHepler).basicStaffValidation(eq(depId), any(StaffLoginDetailsJson.class));
        when(mockValidationStaffHepler.validateAndGetLoginDetails("some@email.com", "somepassword120")).thenReturn(loginDetails);
        when(mockValidationStaffHepler.validateAndGetStaffDetails(eq(depId), any(StaffLoginDetailsJson.class))).thenReturn(staff);
        doThrow( new RuntimeException()).when(mockUseInter).saveNewStaffAndLoginDetails(staff, loginDetails);

        given()
            .contentType("application/json")
            .body( generateStaffDetails() )
        .when()//.log().all()
            .put("/" + depId + "/addNewStaff")
        .then()//.log().all()
            .statusCode(INTERNAL_SERVER_ERROR);
    }

    @Test
    public void searchForStaffTest() throws  Exception{
        doNothing().when(mockValidationStaffHepler).basicValidationOfDepartmentId(1);
        doNothing().when(mockValidationStaffHepler).basicValidationOfSearchValue("david");
        List<Staff> staffs = asList(new Staff(2, 1, "David Smith", commonConv.getDateFromString("1999-01-01"),
                commonConv.getDateFromString("2016-01-01"), null, "Developer", "ds@co.uk", null));
        when(mockUseInter.searchForStaffs(1, "david", SearchType.NAME)).thenReturn(staffs);

        given()
             .queryParam("searchValue", "david")
             .queryParam("type", "name")
        .when()
             .post("/" + 1 + "/searchForStaff")
        .then()//.log().all()
             .statusCode(HTML_OK)
             .body("staffDetailsList[0].staffId", equalTo(2))
                .body("staffDetailsList[0].depId", equalTo(1))
                .body("staffDetailsList[0].fullName", equalTo("David Smith"))
                .body("staffDetailsList[0].dob", equalTo("1999-01-01"))
                .body("staffDetailsList[0].startDay", equalTo("2016-01-01"))
                .body("staffDetailsList[0].lastDay", equalTo(null))
                .body("staffDetailsList[0].position", equalTo("Developer"))
                .body("staffDetailsList[0].staffEmail", equalTo("ds@co.uk"))
                .body("staffDetailsList[0].comment", equalTo(null));
    } // Validation

    @Test
    public void searchForStaffValidationTest() throws  Exception{
        doNothing().when(mockValidationStaffHepler).basicValidationOfDepartmentId(1);
        doNothing().when(mockValidationStaffHepler).basicValidationOfSearchValue("david");

        given()
            .queryParam("searchValue", "david")
            .queryParam("type", "null")
        .when()
            .post("/" + 1 + "/searchForStaff")
        .then()//.log().all()
            .statusCode(BAD_REQUEST)
            .body("message", equalTo("Invalid search type: null"));
    }

    @Test
    public void searchForStaffSQLExceptionTest() throws  Exception{
        doNothing().when(mockValidationStaffHepler).basicValidationOfDepartmentId(1);
        doNothing().when(mockValidationStaffHepler).basicValidationOfSearchValue("david");
        doThrow( new SQLFaultException("Unable to connect to database while searching for staff by name")).when(mockUseInter).searchForStaffs(1, "david", SearchType.NAME);

        given()
            .queryParam("searchValue", "david")
            .queryParam("type", "name")
        .when()
            .post("/" + 1 + "/searchForStaff")
        .then()//.log().all()
            .statusCode(SERVICE_UNAVAILABLE)
            .body("message", equalTo("Unable to connect to database while searching for staff by name"));
    }

    @Test
    public void searchForStaffSQLRuntimeTest() throws  Exception{
        doNothing().when(mockValidationStaffHepler).basicValidationOfDepartmentId(1);
        doNothing().when(mockValidationStaffHepler).basicValidationOfSearchValue("david");
        doThrow( new RuntimeException()).when(mockUseInter).searchForStaffs(1, "david", SearchType.NAME);

        given()
            .queryParam("searchValue", "david")
            .queryParam("type", "name")
        .when()
            .post("/" + 1 + "/searchForStaff")
        .then()//.log().all()
            .statusCode(INTERNAL_SERVER_ERROR);
    }


    private StaffLoginDetailsJson generateStaffDetails() {
        return new StaffLoginDetailsJson("Full Name", "1990-01-01", "2016-01-01",
                "Developer", null, null, "some@email.com", "somepassword120");
    }

}
