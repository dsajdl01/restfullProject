package com.departments.core.data;

import com.department.core.data.PasswordAuthentication;
import com.departments.dto.fault.exception.LoginStaffException;
import com.departments.dto.fault.exception.ValidationException;
import com.httpSession.core.HttpSessionCoreServlet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Created by david on 14/01/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class PasswordAuthenticationTest {

    @Mock HttpSessionCoreServlet httpSessionCoreServlet;
    @Mock HttpServletRequest httpServletRequest;

    private PasswordAuthentication auth;

    @Before
    public void setUp() {
            auth = new PasswordAuthentication(httpSessionCoreServlet);
    }

    @Test
    public void passwordAuthentication_test() throws Exception {
        String hashPassword = auth.hashPassword("somePassword123");

        assertThat(auth.authenticate("somePassword123", hashPassword), is(true));
        assertThat(auth.authenticate("samePassword123", hashPassword), is(false));
    }

    @Test
    public void basicStaffIdValidation_test() {
        try {
            when(httpSessionCoreServlet.getStaffIdAttribute(httpServletRequest)).thenReturn(1);
            doNothing().when(httpSessionCoreServlet).anyStaffIsLogin(httpServletRequest);
            auth.authorizedStaffId(1, httpServletRequest );
        } catch (ValidationException | LoginStaffException e) {
            fail( "basicStaffIdValidation_test: LoginStaffException or ValidationException should NOT be thrown here.");
        }
    }

    @Test
    public void basicStaffIdValidationValError_test() {
        try {
            auth.authorizedStaffId(0, httpServletRequest );
            fail( "basicStaffIdValidationValError_test: ValidationException should be thrown here.");
        } catch (ValidationException | LoginStaffException e) {
            assertThat(e instanceof LoginStaffException, is(false));
            assertThat(e instanceof ValidationException, is(true));
            assertThat(e.getMessage(), is("Staff id cannot be zero or negative"));
        }
    }

    @Test
    public void basicStaffIdValidationLoginError_test() {
        try {
            doNothing().when(httpSessionCoreServlet).anyStaffIsLogin(httpServletRequest);
            when(httpSessionCoreServlet.getStaffIdAttribute(httpServletRequest)).thenReturn(1);
            auth.authorizedStaffId(2, httpServletRequest );
            fail( "basicStaffIdValidationLoginError_test: LoginStaffException should be thrown here.");
        } catch (ValidationException | LoginStaffException e) {
            assertThat(e instanceof LoginStaffException, is(true));
            assertThat(e instanceof ValidationException, is(false));
            assertThat(e.getMessage(), is("Current staff is not authorized"));
        }
    }


}
