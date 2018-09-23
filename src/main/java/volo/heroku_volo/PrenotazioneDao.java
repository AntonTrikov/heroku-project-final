package volo.heroku_volo;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

public interface PrenotazioneDao {

	@POST
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response createPrenotazione(@Context HttpHeaders headers, String is) throws Exception;

	@GET
	@Path("{id}")
	@Produces({"application/json"})
    public Response getPrenotazione(@Context HttpHeaders headers, @PathParam("id") String is) throws Exception;
	
	@GET
	@Path("compagnia/{id}")
	@Produces({"application/json"})
    public Response getPrenotazionePerCompagnia(@Context HttpHeaders headers, @PathParam("id") String is) throws Exception;
	
	@DELETE
	@Path("{id}")
    public Response deletePrenotazoine(@Context HttpHeaders headers, @PathParam("id") String is) throws Exception;
}
