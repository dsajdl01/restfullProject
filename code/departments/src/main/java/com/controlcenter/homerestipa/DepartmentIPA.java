package com.controlcenter.homerestipa;

//import org.slf4j.LoggerFactory;



import com.department.core.config.DepartmentProperties;
import com.departments.ipa.data.Department;
import com.departments.ipa.dep_dbo.DepartmentDBO;
import com.departments.ipa.dep_dbo.DepartmentDBOConnection;
import com.departments.ipa.fault.exception.DepartmentFaultService;
import dep.data.provider.DepartmentImpl;
import com.departments.ipa.common.lgb.CommonConversions;
import com.controlcenter.homerestipa.response.DepartmentErrorJson;
import com.controlcenter.homerestipa.response.DepartmentJson;
import com.controlcenter.homerestipa.response.DepartmentSuccessJson;
import com.controlcenter.homerestipa.response.ListDepartment;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.CacheControl;
// import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.ArrayList;
import java.util.List;


@Path("/dep")
public class DepartmentIPA {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentIPA.class);
    private DepartmentImpl depImpl = new DepartmentImpl(new DepartmentDBO(new DepartmentDBOConnection(new DepartmentProperties().getPropertiesDataConfig()).getDbConnection()));

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
            return success(new ListDepartment(department));
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
            return success(false);
        }
    }

    @PUT
    @Path("createDepartment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDepartment(DepartmentJson dep) {
        LOGGER.info("createDepartment: depName={}, depCreater={}", dep.getDepName(), dep.getCreatedBy());
        try {
            Integer creater = new CommonConversions().concertStringToInteger(dep.getCreatedBy());
            depImpl.createNewDepartment(dep.getDepName(), creater);
            return success( new DepartmentSuccessJson(200, "Ok"));
        }
        catch (DepartmentFaultService e) {
            LOGGER.error("createDepartment fault: {} ", e);
            return error(new DepartmentErrorJson(500, e.getMessage()));
        }
    }

    private Response success(final Object response) {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoTransform(false);
        return Response.ok(response, MediaType.APPLICATION_JSON)
                .cacheControl(cacheControl)
                .header("Pragma", "no-cache")
                .header(HttpHeaders.EXPIRES, "Fri, 01 Jan 1990 00:00:00 GMT")
                .build();
    }

    private Response error(final Object response) {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoTransform(false);
        return Response.ok(response, MediaType.APPLICATION_JSON)
                .cacheControl(cacheControl).header("Pragma", "no-cache")
                .header(HttpHeaders.EXPIRES, "Fri, 01 Jan 1990 00:00:00 GMT").build();
    }
}