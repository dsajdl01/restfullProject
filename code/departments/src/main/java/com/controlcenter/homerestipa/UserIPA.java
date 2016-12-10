package com.controlcenter.homerestipa;

import com.controlcenter.homerestipa.response.StaffJson;
import com.controlcenter.homerestipa.response.UserLoginJson;
import com.departments.ipa.common.lgb.CommonConversions;
import com.departments.ipa.data.LoginStaff;
import com.departments.ipa.fault.exception.DepartmentFaultService;
import dep.data.provider.inter.provider.DepartmentCoreServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
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
    private CommonConversions commonConv = new CommonConversions();

    @Inject
    DepartmentCoreServices coreServices;


    @PUT
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logInUser(UserLoginJson user, @Context HttpServletRequest request) {
        try {

            if ( user == null || commonConv.hasStringValue(user.getEmail()) || commonConv.hasStringValue(user.getPassword()) ) {
                return badRequest("Mandatory argument email or password are missing");
            }

            LOGGER.info("logInUser: email= {}, password={}", user.getEmail(), user.getPassword());
            LoginStaff staff = coreServices.getUserImpl().logInUser(user.getEmail(), user.getPassword());
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
            return sqlConnectionError(departmentFaultService.getMessage());
        } catch (Exception e ) {
            LOGGER.error("loginUser: Exception = {} ", e);
            return internalServerError("logInUser: error occur = " + e.getMessage());
        }
    }

    @GET
    @Path("/emailExist")
    public Response doesEmailExist(@QueryParam("email") String email) {
        try {
            if ( commonConv.hasStringValue(email)) {
                return badRequest("Mandatory argument email is missing");
            }
            return success(coreServices.getUserImpl().doesEmailExist(email));
        }
        catch (DepartmentFaultService e) {
            LOGGER.error("DepartmentFaultService: {}", e);
            return sqlConnectionError(e.getMessage());
        }
        catch (Exception e ) {
            LOGGER.error("Exception: {}", e);
            return internalServerError(e.getMessage());
        }
    }
}
