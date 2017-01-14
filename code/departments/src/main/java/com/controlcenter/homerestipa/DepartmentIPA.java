package com.controlcenter.homerestipa;

import com.controlcenter.homerestipa.provider.RestServices;
import com.controlcenter.homerestipa.response.DepartmentJson;
import com.controlcenter.homerestipa.response.DepartmentSuccessJson;
import com.controlcenter.homerestipa.response.ListDepartmentsJson;
import com.departments.dto.common.lgb.CommonConversions;
import com.departments.dto.data.Department;
import com.departments.dto.fault.exception.LoginStaffException;
import com.departments.dto.fault.exception.SQLFaultException;
import com.departments.dto.fault.exception.ValidationException;
import com.departments.dto.fault.exception.ValueConversionFaultException;
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
import java.util.List;

import static com.controlcenter.homerestipa.cash.control.response.RestResponseHandler.*;

@Path("/dep")
public class DepartmentIPA {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentIPA.class);
    private CommonConversions commonConv = new CommonConversions();

    @Inject
    DepartmentCoreServices coreServices;

    @Inject
    RestServices validationHelper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartment(@QueryParam("depId") Integer depId, @QueryParam("staffId")  Integer staffId, @Context HttpServletRequest request) {
        try {
            validationHelper.getValidationHepler().basicValidationOfDepartmentId(depId);
            coreServices.getPasswordAuthentication().authorizedStaffId(staffId, request);
            Department dep = coreServices.getDepartmentImpl().getDepartment(depId);
            return success(new DepartmentJson(dep.getId(), dep.getName(), dep.getCreater()));
        }
        catch (ValidationException e) {
            LOGGER.error("ValidationException: {}", e);
            return badRequest(e.getMessage());
        }
        catch (LoginStaffException e) {
            LOGGER.error("LoginStaffException: {}", e);
            return forbidden(e.getMessage());
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

    @GET
    @Path("/getListDepartment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartmentsList(@QueryParam("staffId")  int staffId, @Context HttpServletRequest request) {
        LOGGER.info("getListDepartment()");
        try {
            LOGGER.info("List of Department");
            coreServices.getPasswordAuthentication().authorizedStaffId(staffId, request);
            List<Department> dep = coreServices.getDepartmentImpl().getDepartmentList();
            List<DepartmentJson> department = new ArrayList<DepartmentJson>();
            for (Department d : dep) {
                department.add(new DepartmentJson(d.getId(), d.getName(), d.getCreater()));
            }
            return success(new ListDepartmentsJson(department));
        }
        catch (ValidationException | LoginStaffException e) {
                LOGGER.error("ValidationException | LoginStaffException: {}", e);
                return forbidden(e.getMessage());
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

    @GET
    @Path("/checkDepartmentName")
    public Response checkDepartmentName(@QueryParam("depName") String depName, @Context HttpServletRequest request) {
        try {
            LOGGER.info("checkDepartmentName: depName={}", depName);
            coreServices.getHttpSessionCoreServlet().anyStaffIsLogin(request);
            return success(coreServices.getDepartmentImpl().checkDepartmenName(depName));
        }
        catch (LoginStaffException e) {
            LOGGER.error("LoginStaffException: {}", e);
            return forbidden(e.getMessage());
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

    @PUT
    @Path("/createDepartment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveDepartment(@QueryParam("staffId")  int staffId, DepartmentJson dep, @Context HttpServletRequest request) {

        try {
            coreServices.getPasswordAuthentication().authorizedStaffId(staffId, request);
            validationHelper.getValidationHepler().basicDepartmentValidation(dep);
            LOGGER.info("saveDepartment: staffId={}, [depId={} depName={}, depCreater={}]", staffId, dep.getDepId(), dep.getDepName(), dep.getCreatedBy());

            String message;
            if(dep.getDepId() == null) {
                message = createNewDepartment(dep);
            }
            else {
                message = modifyDepartment(dep);
            }
            return success( new DepartmentSuccessJson(200, message));
        }
        catch (ValidationException e) {
            LOGGER.error("ValidationException {}", e.getMessage());
            return badRequest(e.getMessage());
        }
        catch (LoginStaffException e) {
            LOGGER.error("LoginStaffException: {}", e);
            return forbidden(e.getMessage());
        }
        catch (ValueConversionFaultException e) {
            return conflict(e.getMessage());
        }
        catch (SQLFaultException e) {
            LOGGER.error("SQLFaultException: {}", e);
            return sqlConnectionError(e.getMessage());
        }
        catch (Exception e) {
            LOGGER.error("Exception fault: {} ", e);
            return internalServerError("Exception = " + e.getMessage());
        }
    }

    private String modifyDepartment(DepartmentJson dep) throws SQLFaultException {
        coreServices.getDepartmentImpl().modifyDepartmentName(dep.getDepId(), dep.getDepName());
        return dep.getDepName() + " is successfully updated";
    }

    private String createNewDepartment(DepartmentJson dep) throws ValueConversionFaultException, SQLFaultException {
        Integer createrId = commonConv.convertStringToInteger(dep.getCreatedBy());
        coreServices.getDepartmentImpl().createNewDepartment(dep.getDepName(), createrId);
        return dep.getDepName() + " is successfully saved";
    }
}
