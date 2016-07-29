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

import com.controlcenter.homerestipa.DepartmentIPA;
import dep.data.provider.DataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/rest")
public class HomeViewIPA {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeViewIPA.class);
	DataProvider dataPro = new DataProvider();

	@GET
	@Path("getData")
	@Produces("text/html")
	public Response getStartingPage()
	{
		LOGGER.info("detData() RRRRRRRRRRRRRRRRRRRRRRRR");

		String output = dataPro.getMessage();
		return Response.status(200).entity(output).build();
	}
}
