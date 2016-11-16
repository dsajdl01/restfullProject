package com.controlcenter.homerestipa;

import com.controlcenter.homerestipa.response.DepartmentErrorJson;
import com.controlcenter.homerestipa.response.StaffJson;
import com.controlcenter.homerestipa.response.UserLoginJson;
import com.department.core.config.DepartmentProperties;
import com.departments.ipa.data.LoginStaff;
import com.departments.ipa.dep_dbo.DepartmentDBOConnection;
import com.departments.ipa.dep_dbo.UserDBO;
import com.departments.ipa.fault.exception.DepartmentFaultService;
import dep.data.provider.UserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.controlcenter.homerestipa.cash.control.response.RestResponseHandler.*;

/**
 * Created by david on 13/11/16.
 */
@Path("/user")
public class UserIPA {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserIPA.class);
    private UserImpl userImpl = new UserImpl(new UserDBO(new DepartmentDBOConnection(new DepartmentProperties().getPropertiesDataConfig()).getDbConnection()));


    @PUT
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logInUser(UserLoginJson user, @Context HttpServletRequest request){
        try {
            LOGGER.info("logInUser: email= {}, password={}", user.getEmail(), user.getPassword());

            LoginStaff staff = userImpl.logInUser(user.getEmail(), user.getPassword());
            HttpSession session = request.getSession(true);

            if(staff == null) {
                session.invalidate();
                return badRequest("Invalid email or password");
            }

            session.setAttribute("userId", staff.getUserId());
            return success( new StaffJson(staff.getUserId(), staff.getName()));
        }
        catch (DepartmentFaultService departmentFaultService) {
            LOGGER.error("loginUser: DepartmentFaultService = {} ", departmentFaultService);
            return error( new DepartmentErrorJson(500, departmentFaultService.getMessage()));
        } catch (Exception e ) {
            LOGGER.error("loginUser: Exception = {} ", e);
            return internalServerError("logInUser: error occur = " + e.getMessage());
        }
    }
}
