package volo.dao;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public interface TrattaDao {
	@DELETE
	@Path("{id}")
	@Produces({"application/json"})
	public Response deleteTratta(@Context HttpHeaders headers, @PathParam("id") String is) throws Exception;

	@PUT
	@Consumes({"application/json"})
	@Produces({"application/json"})
    public Response putTratta(@Context HttpHeaders headers, String is) throws Exception;
	
	@GET
	@Path("{id}")
	@Produces({"application/json"})
    public Response getTratta(@PathParam("id") String is) throws Exception;
	
	@GET
	@Path("query")
	@Produces({"application/json"})
    public Response getQueryTratta(@Context UriInfo info) throws Exception;

	@GET
	@Path("all")
	@Produces({"application/json"})
    public Response getAllTratte() throws Exception;
	
	@POST
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response createTratta(@Context HttpHeaders headers, String is) throws Exception;
}
