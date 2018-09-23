package volo.impl;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;
import org.json.*;
import javax.ws.rs.core.UriInfo;
import javax.json.*;

import java.sql.*;
import org.apache.commons.codec.binary.Base64;

import volo.dao.PrenotazioneDao;
import volo.entity.Aereo;
import volo.entity.Pg;
import volo.entity.Prenotazione;
import volo.entity.Tratta;

@Path("/prenotazione")
public class PrenotazioneImpl implements PrenotazioneDao{
	@POST
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response createPrenotazione(@Context HttpHeaders headers, String is) throws Exception{
		Response response=null;
		if(!(ImplUtils.isAuthorized(headers)||ImplUtils.isAdmin(headers))) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		Prenotazione prenotazione= readPrenotazione(is);
		int compagnia=prenotazione.getCompagnia();
		int slot=prenotazione.getSlot();
		int tratta=prenotazione.getTratta();
		PreparedStatement query = null;
		Connection conn = null;
		int id=0;
		try {
			conn = Pg.pgConn();
			query = conn.prepareStatement("BEGIN;");
			query.executeUpdate();
			String q="select count(*) from prenotazione where slot="+slot+";";
			query = conn.prepareStatement(q);
			ResultSet rs = query.executeQuery();
			int prenotati=0;
			while(rs.next()) {
				prenotati=rs.getInt("count");
			}
			q="select slots from slot where id="+slot;
			query = conn.prepareStatement(q);
			rs = query.executeQuery();
			int totali=0;
			while(rs.next()) {
				totali=rs.getInt("slots");
			}
			if(totali-prenotati==0) {
				return response = Response.status(Response.Status.BAD_REQUEST).build();
			}
			q="select slot from slotPerTratta where tratta="+tratta+";";
			query = conn.prepareStatement(q);
			rs = query.executeQuery();
			boolean slotEsiste=false;
			while(rs.next()) {
				if(slot==rs.getInt("slots")){
					slotEsiste=true;
				}
			}
			if(!slotEsiste) {
				return response = Response.status(Response.Status.BAD_REQUEST).build();
			}
			q="select aereo from aereiPerSlot where slot="+slot;
			query = conn.prepareStatement(q);
			rs = query.executeQuery();
			List<Integer> aereiSlot = new ArrayList<Integer>();
			while(rs.next()) {
				aereiSlot.add(rs.getInt("aereo"));
			}
			q="select aereo from aereiCompagnie where compagnia="+compagnia;
			query = conn.prepareStatement(q);
			rs = query.executeQuery();
			List<Integer> aereiCompagnia = new ArrayList<Integer>();
			while(rs.next()) {
				aereiCompagnia.add(rs.getInt("aereo"));
			}
			boolean authorized=false;
			for(int aereoAuthorized : aereiSlot) {
				for(int aereoPosseduto : aereiCompagnia) {
					if(aereoAuthorized==aereoPosseduto) {
						authorized=true;
					}
				}
			}
			if(!authorized) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
			q = "insert into prenotazione(compagnia,tratta,slot) values("+compagnia+","+tratta+","+slot+");";
			query = conn.prepareStatement(q);
			query.executeUpdate();
			q = "select max(id) from prenotazione";
			query = conn.prepareStatement(q);
			rs = query.executeQuery();
			while(rs.next()) {
				id=rs.getInt("max");
			}
			query.close();
		} catch(Exception e) {
			e.printStackTrace();
			//String q = "delete from tratta where nomeTratta='"+name+"'";
			String q = "ROLLBACK;";
			PreparedStatement rollback = conn.prepareStatement(q);
			rollback.executeUpdate();
			rollback.close();
			conn.close();
			return  Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}finally {
			if(!conn.isClosed()) {
				String q = "COMMIT;";
				PreparedStatement commit = conn.prepareStatement(q);
				commit.executeUpdate();
				response = Response.created(URI.create("prenotazione/" + id)).build();
				commit.close();
				conn.close();
			}
		}
		return response;
	}
	@GET
	@Path("{id}")
	@Produces({"application/json"})
    public Response getPrenotazione(@Context HttpHeaders headers, @PathParam("id") String is) throws Exception {
		int id = Integer.parseInt(is);
		Response response=null;
		if(!(ImplUtils.isAuthorized(headers)||ImplUtils.isAdmin(headers))) {
			return response = Response.status(Response.Status.UNAUTHORIZED).build();
		}
		PreparedStatement query = null;
		Connection conn = null;
		int compagnia=0; int slot=0; int tratta=0;
		try {
			conn = Pg.pgConn();
			query = conn.prepareStatement("select count(*) from prenotazione where id="+id+";");
			ResultSet rs = query.executeQuery();
			int count=0;
			while(rs.next()) {
				count = Integer.parseInt(rs.getString("count"));
			}
			if(count==0) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			query = conn.prepareStatement("select * from prenotazione where id="+id+";");
			rs = query.executeQuery();
			while(rs.next()) {
				compagnia = Integer.parseInt(rs.getString("compagnia"));
				slot = Integer.parseInt(rs.getString("slot"));
				tratta = Integer.parseInt(rs.getString("tratta"));
			}
			query.close();
		} catch(Exception e) {
			e.printStackTrace();
			response=Response.status(Response.Status.NOT_FOUND).build();
		}finally {
			if(!conn.isClosed()){
				Prenotazione prenotazione = new Prenotazione(id,slot,compagnia,tratta);
				response=Response.ok(prenotazione.toJson()).build();
				conn.close();
			}
		}
		return response;
    }
	@GET
	@Path("compagnia/{id}")
	@Produces({"application/json"})
    public Response getPrenotazionePerCompagnia(@Context HttpHeaders headers, @PathParam("id") String is) throws Exception {
		int id = Integer.parseInt(is);
		Response response=null;
		if(!(ImplUtils.isAuthorized(headers)||ImplUtils.isAdmin(headers))) {
			return response = Response.status(Response.Status.UNAUTHORIZED).build();
		}
		PreparedStatement query = null;
		Connection conn = null;
		int compagnia=0; int slot=0; int tratta=0;
		List<Prenotazione> prenotazioni = new ArrayList<Prenotazione>();
		try {
			conn = Pg.pgConn();
			query = conn.prepareStatement("select count(*) from compagnia where id="+id+";");
			ResultSet rs = query.executeQuery();
			int count=0;
			while(rs.next()) {
				count = Integer.parseInt(rs.getString("count"));
			}
			if(count==0) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			query = conn.prepareStatement("select * from prenotazione where compagnia="+id+";");
			rs = query.executeQuery();
			while(rs.next()) {
				compagnia = Integer.parseInt(rs.getString("compagnia"));
				slot = Integer.parseInt(rs.getString("slot"));
				tratta = Integer.parseInt(rs.getString("tratta"));
				prenotazioni.add(new Prenotazione(id,slot,compagnia,tratta));
			}
			query.close();
		} catch(Exception e) {
			e.printStackTrace();
			response=Response.status(Response.Status.NOT_FOUND).build();
		}finally {
			if(!conn.isClosed()){
				response=Response.ok(Prenotazione.prenotazioniToJson(prenotazioni)).build();
				conn.close();
			}
		}
		return response;
    }
	@DELETE
	@Path("{id}")
    public Response deletePrenotazoine(@Context HttpHeaders headers, @PathParam("id") String is) throws Exception{
		int id =Integer.parseInt(is);
		Response response=null;
		if(!ImplUtils.isAdmin(headers)) {
			return response = Response.status(Response.Status.UNAUTHORIZED).build();
		}
		PreparedStatement query = null;
		Connection conn = null;
		try {
			conn = Pg.pgConn();
			String q = "BEGIN;";
			query = conn.prepareStatement(q);
			query.executeUpdate();
			q = "select count(*) from prenotazione where id="+id+";";
			query = conn.prepareStatement(q);
			ResultSet rs = query.executeQuery();
			int rowcount = 0;
			while(rs.next()) {
				rowcount=rs.getInt("count");
			}
			if(rowcount==0) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			q = "delete from prenotazione where id="+id+";";
			PreparedStatement testo = conn.prepareStatement(q);
			testo.executeUpdate();
			testo.close();
			query.close();
		} catch(Exception e) {
			e.printStackTrace();
			String q = "ROLLBACK;";
			PreparedStatement rollback = conn.prepareStatement(q);
			rollback.executeUpdate();
			rollback.close();
			conn.close();
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}finally {
			if(!conn.isClosed()) {
				String q = "COMMIT;";
				PreparedStatement commit = conn.prepareStatement(q);
				commit.executeUpdate();
				commit.close();
				conn.close();
				return Response.ok().build();
			}
		}
		return response;
    }
	private Prenotazione readPrenotazione(String s) {
		JsonReader jsonReader = Json.createReader(new StringReader(s));
		JsonObject o = jsonReader.readObject();
		int compagnia = o.getInt("compagnia");
		int slot = o.getInt("slot");
		int tratta = o.getInt("tratta");
		return new Prenotazione(slot,compagnia,tratta);
	}
}
