package com.controlcenter.homerestipa;

//import org.slf4j.LoggerFactory;
//import org.slf4j.LoggerFactory;
import dep.data.core_dep.Department;
import dep.data.provider.DepartmentImpl;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
// import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/dep")
public class DepartmentIPA{

//    private static final Logger LOGGER = LoggerFactory.getLogger(Department.class);
    private DepartmentImpl depImpl = new DepartmentImpl();
    @GET
    @Path("getListDepartment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartments()
    {
        List<Department> dep = depImpl.getDepartmentList();
        return Response.status(200).entity("hfhgf").build();
    }
}