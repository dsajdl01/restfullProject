package com.controlcenter.homerestipa;

//import org.slf4j.LoggerFactory;



import com.controlcenter.homerestipa.response.DepartmentErrorJson;
import com.controlcenter.homerestipa.response.DepartmentJson;
import com.controlcenter.homerestipa.response.DepartmentSuccessJson;
import com.controlcenter.homerestipa.response.ListDepartmentsJson;
import com.department.core.config.DepartmentProperties;
import com.departments.ipa.common.lgb.CommonConversions;
import com.departments.ipa.data.Department;
import com.departments.ipa.dep_dbo.DepartmentDBO;
import com.departments.ipa.dep_dbo.DepartmentDBOConnection;
import com.departments.ipa.fault.exception.DepartmentFaultService;
import dep.data.provider.DepartmentImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static com.controlcenter.homerestipa.cash.control.response.RestResponseHandler.error;
import static com.controlcenter.homerestipa.cash.control.response.RestResponseHandler.internalServerError;
import static com.controlcenter.homerestipa.cash.control.response.RestResponseHandler.success;

// import javax.ws.rs.PathParam;


@Path("/dep")
public class DepartmentIPA {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentIPA.class);
    private DepartmentImpl depImpl = new DepartmentImpl(new DepartmentDBO(new DepartmentDBOConnection(new DepartmentProperties().getPropertiesDataConfig()).getDbConnection()));
    private CommonConversions commonConv = new CommonConversions();

    @GET
    @Path("getListDepartment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartments() {
        LOGGER.info("getListDepartment()");
        try {
            LOGGER.info("List of Department");
            List<Department> dep = depImpl.getDepartmentList();

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
            return success(depImpl.checkDepartmenName(depName));
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
                Integer creater = commonConv.concertStringToInteger(dep.getCreatedBy());
                depImpl.createNewDepartment(dep.getDepName(), creater);
            }
            else
            {
                if(!commonConv.hasStringValue(dep.getDepName())) {
                    depImpl.modifyDepartmentName(dep.getDepId(), dep.getDepName());
                } else {
                    LOGGER.error("Modify Department name is empty");
                    return error(new DepartmentErrorJson(400, "Depatrment name cannot be empty"));
                }
            }
            return success( new DepartmentSuccessJson(200, "Ok"));
        }
        catch (DepartmentFaultService e) {
            LOGGER.error("createDepartment fault: {} ", e);
            return error(new DepartmentErrorJson(500, e.getMessage()));
        }
        catch (Exception e) {
            LOGGER.error("Exception fault: {} ", e);
            return internalServerError("Exception = " + e.getMessage());
        }
    }
}
