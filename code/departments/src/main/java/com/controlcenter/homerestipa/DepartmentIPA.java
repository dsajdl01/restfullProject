package com.controlcenter.homerestipa;

import com.controlcenter.homerestipa.response.DepartmentErrorJson;
import com.controlcenter.homerestipa.response.DepartmentJson;
import com.controlcenter.homerestipa.response.DepartmentSuccessJson;
import com.controlcenter.homerestipa.response.ListDepartmentsJson;
import com.departments.ipa.common.lgb.CommonConversions;
import com.departments.ipa.data.Department;
import com.departments.ipa.fault.exception.DepartmentFaultService;
import dep.data.provider.inter.provider.DepartmentCoreServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    @Path("getListDepartment")
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
            return error(new DepartmentErrorJson(500, e.getMessage()));
        }
    }

    @GET
    @Path("checkDepartmentName")
    public Response checkdepartmentName(@QueryParam("depName") String depName) {
        try {
            LOGGER.info("checkDepartmentName: depName={}", depName);
            return success(coreServices.getDepartmentImpl().checkDepartmenName(depName));
        }
        catch (DepartmentFaultService e) {
            LOGGER.error("DepartmentFaultService: {}", e);
            return error(false);
        }
        catch (Exception e ) {
            LOGGER.error("Exception: {}", e);
            return error(false);
        }
    }

    @PUT
    @Path("createDepartment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveDepartment(DepartmentJson dep) {
        LOGGER.info("createDepartment: depId={} depName={}, depCreater={}", dep.getDepId(), dep.getDepName(), dep.getCreatedBy());
        try {
            if(dep.getDepId() == null)
            {
                Integer createrId = commonConv.convertStringToInteger(dep.getCreatedBy());
                coreServices.getDepartmentImpl().createNewDepartment(dep.getDepName(), createrId);
            }
            else
            {
                if(!commonConv.hasStringValue(dep.getDepName())) {
                    coreServices.getDepartmentImpl().modifyDepartmentName(dep.getDepId(), dep.getDepName());
                } else {
                    LOGGER.error("Modify Department name is empty");
                    return badRequest("Depatrment name cannot be empty");
                }
            }
            return success( new DepartmentSuccessJson(200, "Ok"));
        }
        catch (Exception e) {
            LOGGER.error("Exception fault: {} ", e);
            return internalServerError("Exception = " + e.getMessage());
        }
    }
}
