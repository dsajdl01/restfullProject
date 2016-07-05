package com.department.home;
/*
http://tutorial-academy.com/restful-webservice-jersey-maven/
http://tutorial-academy.com/httpservlet-was-not-found/
*/
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
// import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import dep.data.provider.DataProvider;

@Path("/rest")
public class HomeViewIPA {

	@GET
	@Path("getData")
	@Produces("text/html")
	public Response getStartingPage()
	{ 
		DataProvider dataPro = new DataProvider();
		String output = dataPro.getMessage();
		return Response.status(200).entity(output).build();
}
}
