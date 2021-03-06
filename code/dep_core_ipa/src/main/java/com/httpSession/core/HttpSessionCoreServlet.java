package com.httpSession.core;

import com.departments.dto.fault.exception.LoginStaffException;
import com.departments.dto.fault.exception.ValidationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by david on 04/01/17.
 */
public class HttpSessionCoreServlet {

    private final String STAFF_ID = "staffId";
    private final String SELECTED_DEP_ID = "selectedDepId";

    public void setStaffIdAttribute(int staffId, HttpSession session) {
        session.setAttribute(STAFF_ID,staffId);
    }

    public void setSelectedDepIdAttribute(int depId, HttpSession session) {
        session.setAttribute(SELECTED_DEP_ID, depId);
    }

    public Integer getStaffIdAttribute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (Integer) session.getAttribute(STAFF_ID);
    }

    public Integer getSelectedDepIdAttribute(HttpServletRequest request) throws  ValidationException {
        HttpSession session = request.getSession();
        Integer departmentId =  (Integer) session.getAttribute(SELECTED_DEP_ID);
        if ( departmentId == null || departmentId <= 0) throw  new ValidationException("Department is not selected.");
        return departmentId;
    }

    public void anyStaffIsLogin(HttpServletRequest request) throws LoginStaffException {
        if ( getStaffIdAttribute(request) == null ) {
            throw new LoginStaffException("Please login");
        }
    }
}
