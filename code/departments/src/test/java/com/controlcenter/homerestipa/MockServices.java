package com.controlcenter.homerestipa;

import dep.data.provider.CoreServices;
import dep.data.provider.DepartmentImpl;
import dep.data.provider.UserImpl;
import dep.data.provider.inter.provider.DepartmentCoreServices;
import dep.data.provider.inter.provider.DepartmentInter;
import dep.data.provider.inter.provider.UserInter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * Created by david on 04/12/16.
 */
public class MockServices {
    protected static HttpServletRequest mockHttpServletRequest  = mock(HttpServletRequest.class);
    protected static UserInter mockUseInter  = mock(UserImpl.class);
    protected static HttpSession httpSessionMock = mock(HttpSession.class);
    protected static DepartmentCoreServices mockDepartmentCoreServices = mock(CoreServices.class);
    protected static DepartmentInter mockDepartmentInter  = mock(DepartmentImpl.class);

    protected static final int SERVICE_UNAVAILABLE = 503;
    protected static final int INTERNAL_SERVER_ERROR = 500;
    protected static final int HTML_OK = 200;
    protected static final int CONFLICT = 409;
    protected static final int BAD_REQUEST = 400;

    public static void resetMocks() {
        reset(mockHttpServletRequest, mockUseInter, httpSessionMock, mockDepartmentCoreServices, mockDepartmentInter);

        when(mockDepartmentCoreServices.getUserImpl()).thenReturn(mockUseInter);
        when(mockDepartmentCoreServices.getDepartmentImpl()).thenReturn(mockDepartmentInter);
    }

}
