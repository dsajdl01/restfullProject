package com.httpSession.core;

import com.departments.dto.fault.exception.LoginStaffException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.hamcrest.core.Is.is;

/**
 * Created by david on 14/01/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class HttpSessionCoreServletTest {

    @Mock HttpServletRequest httpRequest;

    @Mock HttpSession httpSession;

    private HttpSessionCoreServlet httpCoreSession;

    @Before
    public void setUp() {
        httpCoreSession = new HttpSessionCoreServlet();
    }

    @Test
    public void StaffIdAttribute_test() throws Exception {
        int staffId = 1;
        when(httpRequest.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute("staffId")).thenReturn(staffId);

        httpCoreSession.setStaffIdAttribute(staffId, httpSession);
        assertThat(httpCoreSession.getStaffIdAttribute(httpRequest), is(staffId));
    }

    @Test
    public void departmentIdAttribute_Test() throws Exception {
        int depId = 2;
        when(httpRequest.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute("selectedDepId")).thenReturn(depId);

        httpCoreSession.setSelectedDepIdAttribute(depId, httpSession);
        assertThat(httpCoreSession.getSelectedDepIdAttribute(httpRequest), is(depId));
    }

    @Test
    public void anyStaffIsLogin_test() {
        try {
            int staffId = 1;
            when(httpRequest.getSession()).thenReturn(httpSession);
            when(httpSession.getAttribute("staffId")).thenReturn(staffId);
            httpCoreSession.anyStaffIsLogin(httpRequest);
        } catch (LoginStaffException e ) {
            fail( "anyStaffIsLogin_test: LoginStaffException should NOT be thrown here.");
        }
    }

    @Test
    public void anyStaffIsLoginLoginStaffException_test() {
        try {
            when(httpRequest.getSession()).thenReturn(httpSession);
            when(httpSession.getAttribute("staffId")).thenReturn(null);
            httpCoreSession.anyStaffIsLogin(httpRequest);
            fail( "anyStaffIsLoginLoginStaffException_test: LoginStaffException should be thrown here.");
        } catch (LoginStaffException e ) {
            assertThat(e.getMessage(), is("Please login"));
        }
    }
}
