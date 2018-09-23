package volo.heroku_volo;

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

public interface CompagniaDao {
	@GET
	@Path("{id}")
	@Produces({"application/json"})
    public Response getCompagnia(@Context HttpHeaders headers, @PathParam("id") String is) throws Exception;
	
	@GET
	@Path("all")
	@Produces({"application/json"})
    public Response getAllCompagnie(@Context HttpHeaders headers) throws Exception;

	@DELETE
	@Path("{id}")
	@Produces({"application/json"})
    public Response deleteCompagnia(@Context HttpHeaders headers, @PathParam("id") String is) throws Exception;
	
	@POST
	@Path("register")
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response registerCompagnia(String is) throws Exception;
	
	@POST
	@Path("login")
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response loginCompagnia(@Context HttpHeaders headers) throws Exception;
	
	@PUT
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response updateCompagnia(@Context HttpHeaders headers, String is) throws Exception;
}
