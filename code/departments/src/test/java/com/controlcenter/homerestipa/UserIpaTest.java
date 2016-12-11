package com.controlcenter.homerestipa;

import com.controlcenter.homerestipa.response.UserLoginJson;
import com.department.testutils.JerseyContainerJUnitRule;
import com.departments.ipa.data.LoginStaff;
import com.departments.ipa.fault.exception.DepartmentFaultService;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.jayway.restassured.RestAssured;
import dep.data.provider.inter.provider.DepartmentCoreServices;
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

import static com.controlcenter.homerestipa.MockServices.*;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Created by david on 04/12/16.
 */
public class UserIpaTest {

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
        LoginStaff loginStaff = new LoginStaff(1, "Fred Smith");
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
            .body("name", equalTo("Fred Smith"));

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
        LoginStaff loginStaff = new LoginStaff(1, "Fred Smith");
        doThrow(new DepartmentFaultService("Enable to connect to database")).when(mockUseInter).logInUser("smith@fred.com","password");
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
        LoginStaff loginStaff = new LoginStaff(1, "Fred Smith");
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
        doThrow(new DepartmentFaultService("Enable to connect to database")).when(mockUseInter).doesEmailExist(email);

        given()
            .queryParam("email", email)
        .when()//.log().all()
             .get("/emailExist")
        .then()//.log().all()
            .statusCode(SERVICE_UNAVAILABLE)
            .body("message", equalTo("Enable to connect to database"));
    }
}
