package com.controlcenter.homerestipa;

import com.controlcenter.homerestipa.response.DepartmentJson;
import com.controlcenter.homerestipa.response.DepartmentSuccessJson;
import com.controlcenter.homerestipa.response.ListDepartmentsJson;
import com.departments.ipa.common.lgb.CommonConversions;
import com.departments.ipa.data.Department;
import com.departments.ipa.fault.exception.DepartmentFaultService;
import com.departments.ipa.fault.exception.DepartmentValueConversionFault;
import dep.data.provider.inter.provider.DepartmentCoreServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
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

    @GET
    @Path("/getListDepartment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartments() {
        LOGGER.info("getListDepartment()");
        try {
            LOGGER.info("List of Department");
            List<Department> dep = coreServices.getDepartmentImpl().getDepartmentList();

            List<DepartmentJson> department = new ArrayList<DepartmentJson>();
            for (Department d : dep) {
                department.add(new DepartmentJson(d.getId(), d.getName(), d.getCreater()));
            }
            return success(new ListDepartmentsJson(department));
        } catch (DepartmentFaultService e) {
            LOGGER.error("DepartmentFaultService: {}", e);
            return sqlConnectionError(e.getMessage());
        }
        catch (Exception e ) {
            LOGGER.error("Exception: {}", e);
            return internalServerError(e.getMessage());
        }
    }

    @GET
    @Path("/checkDepartmentName")
    public Response checkdepartmentName(@QueryParam("depName") String depName) {
        try {
            LOGGER.info("checkDepartmentName: depName={}", depName);
            return success(coreServices.getDepartmentImpl().checkDepartmenName(depName));
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

    @PUT
    @Path("/createDepartment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveDepartment(DepartmentJson dep) {
        LOGGER.info("createDepartment: depId={} depName={}, depCreater={}", dep.getDepId(), dep.getDepName(), dep.getCreatedBy());
        try {
            String message;
            if ( dep == null || commonConv.hasStringValue(dep.getDepName())) {
                return  badRequest("Mandatory argument department name is missing");
            }

            if(dep.getDepId() == null)
            {
                Integer createrId = commonConv.convertStringToInteger(dep.getCreatedBy());
                coreServices.getDepartmentImpl().createNewDepartment(dep.getDepName(), createrId);
                message = dep.getDepName() + " is successfully saved";
            }
            else
            {
                coreServices.getDepartmentImpl().modifyDepartmentName(dep.getDepId(), dep.getDepName());
                message = dep.getDepName() + " is successfully updated";
            }
            return success( new DepartmentSuccessJson(200, message));
        }
        catch (DepartmentValueConversionFault e) {
            return conflict(e.getMessage());
        }
        catch (DepartmentFaultService e) {
            LOGGER.error("DepartmentFaultService: {}", e);
            return sqlConnectionError(e.getMessage());
        }
        catch (Exception e) {
            LOGGER.error("Exception fault: {} ", e);
            return internalServerError("Exception = " + e.getMessage());
        }
    }
}
