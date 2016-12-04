package com.controlcenter.homerestipa;

import com.controlcenter.homerestipa.response.DepartmentJson;
import com.department.testutils.JerseyContainerJUnitRule;
import com.departments.ipa.data.Department;
import com.departments.ipa.fault.exception.DepartmentFaultService;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.jayway.restassured.RestAssured;
import dep.data.provider.CoreServices;
import dep.data.provider.DepartmentImpl;
import dep.data.provider.inter.provider.DepartmentCoreServices;
import dep.data.provider.inter.provider.DepartmentInter;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.*;
/**
 * Created by david on 21/11/16.
 */
public class DepartmentIpaTest {

    private static HttpServletRequest mockHttpServletRequest  = mock(HttpServletRequest.class);
    private static DepartmentInter mockDepartmentInter  = mock(DepartmentImpl.class);
    private static DepartmentCoreServices mockDepartmentCoreServices = mock(CoreServices.class);
    private final int SERVICE_UNAVAILABLE = 503;
    private final int INTERNAL_SERVER_ERROR = 500;
    private final int HTML_OK = 200;
    private final int CONFLICT = 409;
    private final int BAD_REQUEST = 400;

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
        RestAssured.basePath = "/dep";
        mockDepartmentInter  = mock(DepartmentImpl.class);
    }

    @Test
    public void getDepartmentsTest() throws Exception {
        List<Department> departmentList = Arrays.asList( new Department(1, "IT Team", "Bob Marley") );
        when(mockDepartmentCoreServices.getDepartmentImpl()).thenReturn(mockDepartmentInter);
        when(mockDepartmentInter.getDepartmentList()).thenReturn(departmentList);

        given()
           .when()//.log().all()
           .get("/getListDepartment")
        .then()//.log().all()
            .statusCode(HTML_OK)
            .body("department[0].depId",  equalTo(1))
            .body("department[0].depName",  equalTo("IT Team"))
            .body("department[0].createdBy",  equalTo("Bob Marley"));
    }

    @Test
    public void getDepartmentsSQLErrorTest() throws Exception {

        when(mockDepartmentCoreServices.getDepartmentImpl()).thenReturn(mockDepartmentInter);
        doThrow(new DepartmentFaultService("Inable to connect to database")).when(mockDepartmentInter).getDepartmentList();

        given()
            .when()//.log().all()
            .get("/getListDepartment")
        .then()//.log().all()
            .statusCode(SERVICE_UNAVAILABLE)
            .body("message", equalTo("Inable to connect to database"));
    }

    @Test
    public void getDepartmentsErrorTest() throws Exception {

        when(mockDepartmentCoreServices.getDepartmentImpl()).thenReturn(mockDepartmentInter);
        doThrow(new RuntimeException()).when(mockDepartmentInter).getDepartmentList();

        given()
            .when()//.log().all()
            .get("/getListDepartment")
        .then()//.log().all()
            .statusCode(INTERNAL_SERVER_ERROR);
    }

    @Test
    public void checkdepartmentNameTest() throws Exception {
        String name = "IT Deapartment";
        when(mockDepartmentCoreServices.getDepartmentImpl()).thenReturn(mockDepartmentInter);
        when(mockDepartmentInter.checkDepartmenName(name)).thenReturn(true);

        given()
            .queryParam("depName", name)
        .when()//.log().all()
            .get("/checkDepartmentName")
        .then()//.log().all()
            .statusCode(HTML_OK)
            .body(equalTo("true"));
    }


    @Test
    public void checkdepartmentNameSQLErrorTest() throws Exception {
        String name = "IT Deapartment";
        when(mockDepartmentCoreServices.getDepartmentImpl()).thenReturn(mockDepartmentInter);
        doThrow(new DepartmentFaultService("Inable to connect to database")).when(mockDepartmentInter).checkDepartmenName(name);

        given()
            .queryParam("depName", name)
        .when()//.log().all()
            .get("/checkDepartmentName")
        .then()//.log().all()
            .statusCode(SERVICE_UNAVAILABLE)
            .body("message", equalTo("Inable to connect to database"));
    }

    @Test
    public void checkdepartmentNameErrorTest() throws Exception {
        String name = "IT Deapartment";
        when(mockDepartmentCoreServices.getDepartmentImpl()).thenReturn(mockDepartmentInter);
        doThrow(new RuntimeException()).when(mockDepartmentInter).checkDepartmenName(name);

        given()
            .queryParam("depName", name)
        .when()//.log().all()
            .get("/checkDepartmentName")
        .then()//.log().all()
            .statusCode(INTERNAL_SERVER_ERROR);
    }

    @Test
    public void saveDepartmentTest() throws Exception {
        String name = "Network Team";
        when(mockDepartmentCoreServices.getDepartmentImpl()).thenReturn(mockDepartmentInter);
        doNothing().when(mockDepartmentInter).createNewDepartment(name, 1);
        DepartmentJson dep = new DepartmentJson(null, name, "1");

        given()
            .contentType("application/json")
            .body( dep )
        .when()//.log().all()
            .put("/createDepartment")
        .then()//.log().all()
            .statusCode(HTML_OK)
                .body("message", equalTo(name + " is successfully saved"));
    }

    @Test
    public void saveDepartment_ToModifyTest() throws Exception {
        String name = "Networking Team";
        when(mockDepartmentCoreServices.getDepartmentImpl()).thenReturn(mockDepartmentInter);
        doNothing().when(mockDepartmentInter).modifyDepartmentName(1, name);
        DepartmentJson dep = new DepartmentJson(1, name, "1");

        given()
            .contentType("application/json")
            .body( dep )
        .when()//.log().all()
            .put("/createDepartment")
        .then()//.log().all()
            .statusCode(HTML_OK)
            .body("message", equalTo(name + " is successfully updated"));
    }

    @Test
    public void saveDepartment_ConflintTest() throws Exception {
        String name = "Network Team";
        DepartmentJson dep = new DepartmentJson(null, name, "cf-1");

        given()
            .contentType("application/json")
            .body( dep )
        .when()//.log().all()
            .put("/createDepartment")
        .then()//.log().all()
            .statusCode(CONFLICT)
            .body("message", equalTo("String must contains only digits, cf-1"));
    }

    @Test
    public void saveDepartment_badRequestTest() throws Exception {
        DepartmentJson dep = new DepartmentJson(null, null, "1");

        given()
            .contentType("application/json")
            .body( dep )
        .when()//.log().all()
            .put("/createDepartment")
        .then()//.log().all()
            .statusCode(BAD_REQUEST)
            .body("message", equalTo("Mandatory argument department name is missing"));
    }

    @Test
    public void saveDepartment_SQLErrorTest() throws Exception {
        String name = "Network Team";
        when(mockDepartmentCoreServices.getDepartmentImpl()).thenReturn(mockDepartmentInter);
        doThrow(new DepartmentFaultService("Enable to connect to database")).when(mockDepartmentInter).createNewDepartment(name, 1);
        DepartmentJson dep = new DepartmentJson(null, name, "1");

        given()
            .contentType("application/json")
            .body( dep )
        .when()//.log().all()
            .put("/createDepartment")
        .then()//.log().all()
            .statusCode(SERVICE_UNAVAILABLE)
            .body("message", equalTo("Enable to connect to database"));
    }

    @Test
    public void saveDepartment_InternalErrorTest() throws Exception {
        String name = "Network Team";
        when(mockDepartmentCoreServices.getDepartmentImpl()).thenReturn(mockDepartmentInter);
        doThrow(new RuntimeException()).when(mockDepartmentInter).createNewDepartment(name, 1);
        DepartmentJson dep = new DepartmentJson(null, name, "1");

        given()
            .contentType("application/json")
            .body( dep )
        .when()//.log().all()
            .put("/createDepartment")
        .then()//.log().all()
            .statusCode(INTERNAL_SERVER_ERROR);
    }
}
