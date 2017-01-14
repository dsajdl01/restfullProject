package com.controlcenter.homerestipa;

import com.controlcenter.homerestipa.provider.RestServices;
import com.controlcenter.homerestipa.response.*;
import com.departments.dto.common.lgb.CommonConversions;
import com.departments.dto.common.lgb.SearchType;
import com.departments.dto.data.LoginDetails;
import com.departments.dto.data.LoginStaff;
import com.departments.dto.data.Staff;
import com.departments.dto.fault.exception.SQLFaultException;
import com.departments.dto.fault.exception.ValidationException;
import dep.data.core.provider.inter.provider.DepartmentCoreServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Inject
    RestServices validationHelper;


    @PUT
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logInUser(UserLoginJson user, @Context HttpServletRequest request) {
        try {

            LOGGER.info("logInUser: user.mail={}", user.getEmail());
            validationHelper.getValidationHepler().basicValidateEmailAndPasswordLogin(user.getEmail(), user.getPassword());
            LoginStaff staff = coreServices.getUserImpl().logInUser(user.getEmail(), user.getPassword(), request.getSession(true));
            return success( new LoginStaffJson(staff.getUserId(), staff.getName(), staff.getFirstLogin()));

        } catch (ValidationException e) {
                LOGGER.error("logInUser: ValidationException = {} ", e.getMessage());
                return badRequest(e.getMessage());

        } catch (SQLFaultException departmentFaultService) {
            LOGGER.error("loginUser: SQLFaultException = {} ", departmentFaultService);
            return sqlConnectionError(departmentFaultService.getMessage());

        } catch (Exception e ) {
            LOGGER.error("loginUser: Exception = {} ", e);
            return internalServerError("logInUser: error occur = " + e.getMessage());
        }
    }

    @PUT
    @Path("/{depId}/addNewStaff")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewStaff(@PathParam("depId") final Integer depId, StaffLoginDetailsJson newStaff, @Context HttpServletRequest request) {
        try {
            validationHelper.getValidationHepler().basicStaffValidation(depId, newStaff);
            coreServices.getDepartmentImpl().doesDepartmentExist(depId);
            LOGGER.info("addNewStaff: depId={}, new staff fullName={}", depId, newStaff.getFullName());
            LoginDetails loginDetail = validationHelper.getValidationHepler().validateAndGetLoginDetails(newStaff.getLoginEmail(), newStaff.getPassword());
            Staff staff = validationHelper.getValidationHepler().validateAndGetStaffDetails(depId, newStaff);
            coreServices.getUserImpl().saveNewStaffAndLoginDetails(staff, loginDetail);
            return success();
        } catch (ValidationException e) {
            LOGGER.error("addNewStaff: ValidationException = {} ", e.getMessage());
            return badRequest(e.getMessage());
        } catch (SQLFaultException e) {
                LOGGER.error("SQLFaultException: {}", e);
                return sqlConnectionError(e.getMessage());
        }catch (Exception e ) {
            LOGGER.error("addNewStaff: Exception = {} ", e);
            return internalServerError("addNewStaff: error occur = " + e.getMessage());
        }
    }

    @PUT
    @Path("/modifyStaff")
    public Response modifyStaff(StaffJson staff, @Context HttpServletRequest request) {
        try {
            int id = 0;
      //      coreServices.getPasswordAuthentication().authorizedStaffId(id , request);
            validationHelper.getValidationHepler().basicStaffValidation(staff);
            Staff staffToModify = validationHelper.getValidationHepler().validateMandatoryStaffDetailsAndMapStaff(staff);
            coreServices.getUserImpl().modifyStaffDetails(staffToModify);
            return success();
        } catch (ValidationException e) {
            LOGGER.error("modifyStaff: ValidationException = {} ", e.getMessage());
            return badRequest(e.getMessage());
        } catch (Exception e ) {
            LOGGER.error("modifyStaff: Exception = {} ", e);
            return internalServerError("modifyStaff: error occur = " + e.getMessage());
        }
    }

    @POST
    @Path("/{depId}/searchForStaff")
    public Response searchForStaff(@PathParam("depId") final Integer depId, @QueryParam("searchValue") String searchValue, @QueryParam("type") String type ) {
        LOGGER.info("searchForStaff: depId={}, searchValue={}, type={}", depId, searchValue, type);
        try {
            validationHelper.getValidationHepler().basicValidationOfDepartmentId(depId);
            validationHelper.getValidationHepler().basicValidationOfSearchValue(searchValue);
            coreServices.getDepartmentImpl().doesDepartmentExist(depId);
            List<Staff> staffs = coreServices.getUserImpl().searchForStaffs(depId, searchValue, SearchType.fromString(type));
            List<StaffJson> staffLoginDetailsJsons = new ArrayList<>();
            staffs.forEach( s -> staffLoginDetailsJsons.add( new StaffJson(s.getId(), s.getDepId(), s.getName(), getString(s.getDob()),
                            getString(s.getStartDay()), getString(s.getLastDay()), s.getPosition(), s.getEmail(), s.getComment())));
            return success(new StaffDetailsListJson(staffLoginDetailsJsons));
        } catch (ValidationException e) {
            LOGGER.error("addNewStaff: ValidationException = {} ", e.getMessage());
            return badRequest(e.getMessage());
        } catch (SQLFaultException e) {
            LOGGER.error("SQLFaultException: {}", e);
            return sqlConnectionError(e.getMessage());
        }catch (Exception e ) {
            LOGGER.error("addNewStaff: Exception = {} ", e);
            return internalServerError("logInUser: error occur = " + e.getMessage());
        }
    }

    private String getString(Date date) {
            return date == null ? null : commonConv.convertDateToString(date);
    }


    @GET
    @Path("/emailExist")
    public Response doesEmailExist(@QueryParam("email") String email) {
        try {
            if ( commonConv.stringIsNullOrEmpty(email)) {
                return badRequest("Mandatory argument email is missing");
            }
            return success(coreServices.getUserImpl().doesEmailExist(email));
        }
        catch (SQLFaultException e) {
            LOGGER.error("SQLFaultException: {}", e);
            return sqlConnectionError(e.getMessage());
        }
        catch (Exception e ) {
            LOGGER.error("Exception: {}", e);
            return internalServerError(e.getMessage());
        }
    }
}
