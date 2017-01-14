package com.controlcenter.homerestipa;

import com.controlcenter.homerestipa.provider.RestCenterServices;
import com.controlcenter.homerestipa.provider.RestServices;
import com.controlcenter.homerestipa.utils.ValidationHepler;
import com.department.core.data.PasswordAuthentication;
import com.httpSession.core.HttpSessionCoreServlet;
import dep.data.core.provider.CoreServices;
import dep.data.core.provider.DepartmentImpl;
import dep.data.core.provider.UserImpl;
import dep.data.core.provider.inter.provider.DepartmentCoreServices;
import dep.data.core.provider.inter.provider.DepartmentInter;
import dep.data.core.provider.inter.provider.UserInter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

/**
 * Created by david on 04/12/16.
 */
public class MockServices {
    protected static HttpServletRequest mockHttpServletRequest  = mock(HttpServletRequest.class);
    protected static UserInter mockUseInter  = mock(UserImpl.class);
    protected static HttpSession httpSessionMock = mock(HttpSession.class);
    protected static DepartmentCoreServices mockDepartmentCoreServices = mock(CoreServices.class);
    protected static RestServices mockRestServices = mock(RestCenterServices.class);
    protected static DepartmentInter mockDepartmentInter  = mock(DepartmentImpl.class);
    protected static ValidationHepler mockValidationHepler = mock(ValidationHepler.class);
    protected static HttpSessionCoreServlet mockHttpSessionCoreServlet = mock(HttpSessionCoreServlet.class);
    protected static PasswordAuthentication mockPasswordAuthentication = mock(PasswordAuthentication.class);

    protected static final int SERVICE_UNAVAILABLE = 503;
    protected static final int INTERNAL_SERVER_ERROR = 500;
    protected static final int HTML_OK = 200;
    protected static final int CONFLICT = 409;
    protected static final int BAD_REQUEST = 400;
    protected static final int FORBIDDEN = 403;

    public static void resetMocks() {
        reset(mockHttpServletRequest, mockUseInter, httpSessionMock, mockDepartmentCoreServices, mockDepartmentInter,
                mockRestServices, mockValidationHepler, mockHttpServletRequest, mockPasswordAuthentication);

        when(mockDepartmentCoreServices.getUserImpl()).thenReturn(mockUseInter);
        when(mockDepartmentCoreServices.getDepartmentImpl()).thenReturn(mockDepartmentInter);
        when(mockDepartmentCoreServices.getHttpSessionCoreServlet()).thenReturn(mockHttpSessionCoreServlet);
        when(mockDepartmentCoreServices.getPasswordAuthentication()).thenReturn(mockPasswordAuthentication);
        when(mockRestServices.getValidationHepler()).thenReturn(mockValidationHepler);
    }

}
