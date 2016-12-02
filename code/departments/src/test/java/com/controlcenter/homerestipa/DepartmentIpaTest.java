package com.controlcenter.homerestipa;

import com.department.testutils.JerseyContainerJUnitRule;
import com.departments.ipa.data.Department;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static com.jayway.restassured.RestAssured.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * Created by david on 21/11/16.
 */
public class DepartmentIpaTest {

    private static HttpServletRequest mockHttpServletRequest  = mock(HttpServletRequest.class);
    private static DepartmentInter mockDepartmentInter  = mock(DepartmentImpl.class);
    private static DepartmentCoreServices mockDepartmentCoreServices = mock(CoreServices.class);

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
            .statusCode(200)
            .body("department[0].depId",  equalTo(1))
            .body("department[0].depName",  equalTo("IT Team"))
            .body("department[0].createdBy",  equalTo("Bob Marley"));
    }

}
