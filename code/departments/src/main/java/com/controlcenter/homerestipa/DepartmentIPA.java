package com.controlcenter.homerestipa;

//import org.slf4j.LoggerFactory;


import dep.data.core_dep.Department;
import dep.data.provider.DepartmentImpl;
import com.controlcenter.homerestipa.response.DepartmentJson;
import com.controlcenter.homerestipa.response.ListDepartment;
import javax.ws.rs.core.HttpHeaders;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
// import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.ArrayList;
import java.util.List;


@Path("/dep")
public class DepartmentIPA{

//
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentIPA.class);
    private DepartmentImpl depImpl = new DepartmentImpl();

    @GET
    @Path("getListDepartment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartments()
    {
        LOGGER.info("getListDepartment()");
        List<Department> dep = depImpl.getDepartmentList();
        List<DepartmentJson> department = new ArrayList<DepartmentJson>();
        for(Department d : dep){
            department.add(new DepartmentJson(d.getId(), d.getName(), depImpl.getCreaterName(new Integer(d.getCreatedBy()))));
        }
        ListDepartment listDep = new ListDepartment(department);
        LOGGER.info("List of Department {}", listDep);
        return  Response.ok(listDep, MediaType.APPLICATION_JSON ).build(); // success((Object) listDep);
    }

    private Response success(final Object response)
    {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoTransform(false);
        return Response.ok(response, MediaType.APPLICATION_JSON)
                .cacheControl(cacheControl).header("Pragma", "no-cache")
                .header(HttpHeaders.EXPIRES, "Fri, 01 Jan 1990 00:00:00 GMT").build();
    }
}