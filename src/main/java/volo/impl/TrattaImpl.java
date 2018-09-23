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
import javax.json.JsonReader;
import java.sql.*;
import org.apache.commons.codec.binary.Base64;

import volo.dao.TrattaDao;
import volo.entity.Pg;
import volo.entity.Slot;
import volo.entity.Tratta;

@Path("/tratta")
public class TrattaImpl implements TrattaDao{
	
	@DELETE
	@Path("{id}")
	@Produces({"application/json"})
    public Response deleteTratta(@Context HttpHeaders headers, @PathParam("id") String is) throws Exception{
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
			q = "select count(*) from tratta where id="+id+";";
			query = conn.prepareStatement(q);
			ResultSet rs = query.executeQuery();
			int rowcount = 0;
			while(rs.next()) {
				rowcount=rs.getInt("count");
			}
			if(rowcount==0) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			q = "delete from tratta where id="+id+";";
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
	protected Tratta readTratta(boolean id,String s) {
		JsonReader jsonReader = Json.createReader(new StringReader(s));
		JsonObject o = jsonReader.readObject();
		int trattaId=0;
		if(id==true) {
			trattaId = o.getInt("id");
		}
		String name = o.getString("name");
		String type = o.getString("type");
		String partenza = o.getString("from");
		String arrivo = o.getString("to");
		JsonArray slotsJson = o.getJsonArray("slots");
		List<JsonValue> list = (List<JsonValue>) slotsJson;
		List<Integer> slots = new ArrayList<Integer>();
		for(int i=0;i<list.size();i++) {
			slots.add(Integer.parseInt(list.get(i).toString()));
		}
		Tratta tratta = null;
		if(id==true) {
			tratta = new Tratta(trattaId,name,type,partenza,arrivo,slots);
		}else {
			tratta = new Tratta(name,type,partenza,arrivo,slots);
		}	
		return tratta;
	}
	/*
	
	
	protected String readId(String s) {
		JsonReader jsonReader = Json.createReader(new StringReader(s));
		JsonObject o = jsonReader.readObject();
		return o.getString("id");
	}*/
	
	/*
	String q = "select count(*) from persons where id="+id;
			query = conn.prepareStatement(q);
			ResultSet rs = query.executeQuery();
			int rowcount = 0;
			while(rs.next()) {
				rowcount=rs.getInt("count");
			}
			if(rowcount==0) {
				return Response.status(Response.Status.NOT_FOUND).build();
			} 
	*/
	@PUT
	@Consumes({"application/json"})
	@Produces({"application/json"})
    public Response putTratta(@Context HttpHeaders headers, String is) throws Exception{
		Response response=null;
		if(!(ImplUtils.isAdmin(headers))) {
			return response = Response.status(Response.Status.UNAUTHORIZED).build();
		}
		Tratta tratta = readTratta(true,is);
		int trattaId = tratta.getId();
		String name=tratta.getName();
		String type=tratta.getType();
		String from=tratta.getPartenza();
		String to=tratta.getArrivo();
		List<Integer> slots = tratta.getSlot();
		PreparedStatement query = null;
		Connection conn = null;
		try {
			conn = Pg.pgConn();
			String q="BEGIN;";
			query = conn.prepareStatement(q);
			query.executeUpdate();
			q = "select count(*) from tratta where id="+trattaId+";";
			query = conn.prepareStatement(q);
			ResultSet rs = query.executeQuery();
			int rowcount = 0;
			while(rs.next()) {
				rowcount=rs.getInt("count");
			}
			if(rowcount==0) {
				return Response.status(Response.Status.NOT_FOUND).build();
			} 
			q="update tratta set trattaname='"+name+"',type='"+type+"',partenza='"+from+"',arrivo='"+to+"' where id="+trattaId+";";
			query = conn.prepareStatement(q);
			query.executeUpdate();
			q = "delete from slotPerTratta where tratta="+trattaId+";";
			PreparedStatement testo = conn.prepareStatement(q);
			testo.executeUpdate();
			for(int i=0;i<slots.size();i++) {
				q ="insert into slotPerTratta values("+trattaId+","+slots.get(i)+");";
				PreparedStatement QUERY = conn.prepareStatement(q);
				QUERY.executeUpdate();
				QUERY.close();
			}
			testo.close();
			query.close();
		} catch(Exception e) {
			e.printStackTrace();
			String q = "ROLLBACK;";
			PreparedStatement rollback = conn.prepareStatement(q);
			rollback.executeUpdate();
			rollback.close();
			conn.close();
			response = Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}finally {
			if(conn!=null) {
				if(!conn.isClosed()) {
					String q = "COMMIT;";
					PreparedStatement commit = conn.prepareStatement(q);
					commit.executeUpdate();
					commit.close();
					conn.close();
					response = Response.ok("tratta/" + trattaId).build();
				}
			}
		}
		return response;
    }
	
	@GET
	@Path("{id}")
	@Produces({"application/json"})
    public Response getTratta(@PathParam("id") String is) throws Exception {
		int id = Integer.parseInt(is);
		PreparedStatement query = null;
		Connection conn = null;
		String type=null; String partenza=null; String arrivo=null;String name=null;
		List<Slot> slotTratta= new ArrayList<Slot>();
		Response response=null;
		try {
			conn = Pg.pgConn();
			query = conn.prepareStatement("select count(*) from tratta where id="+id+";");
			ResultSet rs = query.executeQuery();
			int rowcount = 0;
			while(rs.next()) {
				rowcount=rs.getInt("count");
			}
			if(rowcount==0) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			query = conn.prepareStatement("select * from tratta where id="+id+"");
			rs = query.executeQuery();
			while(rs.next()) {
				name = rs.getString("trattaname");
				type = rs.getString("type");
				partenza = rs.getString("partenza");
				arrivo = rs.getString("arrivo");
			}
			PreparedStatement q = conn.prepareStatement("select id,slotname,slots,day,inizio,fine from slotPerTratta join slot on slotPerTratta.slot = slot.id where tratta='"+id+"'");
			ResultSet result = q.executeQuery();
			
			while(result.next()) {
				int slotId = Integer.parseInt(result.getString("id"));
				int slots = Integer.parseInt(result.getString("slots"));
				String nameSlot = result.getString("slotname");
				String day = result.getString("day");
				String inizio = result.getString("inizio");
				String fine = result.getString("fine");
				PreparedStatement QUERY = conn.prepareStatement("select aereo from aereiPerSlot where slot='"+slotId+"'");
				ResultSet risultato = QUERY.executeQuery();
				List<Integer> accepted_planes=new ArrayList<Integer>();
				while(risultato.next()) {
					accepted_planes.add(Integer.parseInt(risultato.getString("aereo")));
				}
				slotTratta.add(new Slot(slotId,nameSlot,slots,day,inizio,fine,accepted_planes));
				//;
			}
			q.close();
			query.close();
		} catch(Exception e) {
			e.printStackTrace();
			response=Response.status(Response.Status.NOT_FOUND).build();
		}finally {
			if(conn!=null){
				Tratta tratta = new Tratta(id,name,type,partenza,arrivo);
				response=Response.ok(tratta.toJson(slotTratta)).build();
				//response=Response.ok(slotTratta.toString()).build();
				conn.close();
			}
		}
		return response;
    }
	/*
		
	 	int i=0;
		String searchQuery="select * from tratta";
		if(partenzaQuery!=null) {
			searchQuery
		}
	 */
	@GET
	@Path("query")
	@Produces({"application/json"})
    public Response getQueryTratta(@Context UriInfo info) throws Exception {
		String partenzaQuery = info.getQueryParameters().getFirst("partenza");
		String arrivoQuery = info.getQueryParameters().getFirst("arrivo");
		String tipologiaQuery = info.getQueryParameters().getFirst("tipologia");
		//String aereoQuery = info.getQueryParameters().getFirst("aereo"); 
		Response response=null;
		PreparedStatement query = null;
		Connection conn = null;
		String type=null; String partenza=null; String arrivo=null;String name=null;int id=0;
		List<Tratta> tratte = new ArrayList<Tratta>();
		try {
			conn = Pg.pgConn();
			String querySearch ="select * from tratta where ";
			if(partenzaQuery!=null) {
				querySearch+=" partenza='"+partenzaQuery+"' AND";
			}else if(arrivoQuery!=null) {
				querySearch+=" arrivo='"+arrivoQuery+"' AND";
			}else if(tipologiaQuery!=null) {
				querySearch+=" type='"+tipologiaQuery+"' AND";
			}
			query = conn.prepareStatement(querySearch.substring(0,querySearch.length()-4)+";");
			ResultSet rs = query.executeQuery();
			while(rs.next()) {
				id = rs.getInt("id");
				name = rs.getString("trattaname");
				type = rs.getString("type");
				partenza = rs.getString("partenza");
				arrivo = rs.getString("arrivo");
				PreparedStatement q = conn.prepareStatement("select id,slotname,slots,day,inizio,fine from slotPerTratta join slot on slotPerTratta.slot = slot.id where tratta='"+id+"'");
				ResultSet result = q.executeQuery();
				List<Slot> slotTratta= new ArrayList<Slot>();
				while(result.next()) {
					int slotId = Integer.parseInt(result.getString("id"));
					int slots = Integer.parseInt(result.getString("slots"));
					String nameSlot = result.getString("slotname");
					String day = result.getString("day");
					String inizio = result.getString("inizio");
					String fine = result.getString("fine");
					PreparedStatement QUERY = conn.prepareStatement("select aereo from aereiPerSlot where slot='"+slotId+"'");
					ResultSet risultato = QUERY.executeQuery();
					List<Integer> accepted_planes=new ArrayList<Integer>();
					
					while(risultato.next()) {
						accepted_planes.add(Integer.parseInt(risultato.getString("aereo")));
					}
					slotTratta.add(new Slot(slotId,nameSlot,slots,day,inizio,fine,accepted_planes));
				}
				tratte.add(new Tratta(id, name, type, partenza, slotTratta,arrivo) );
				q.close();
			}
			query.close();
		} catch(Exception e) {
			e.printStackTrace();
			response=Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}finally {
			if(conn!=null){
				response=Response.ok(tratteToJson(tratte)).build();
				//response=Response.ok(slotTratta.toString()).build();
				conn.close();
			}
		}
		return response;
    }
	@GET
	@Path("all")
	@Produces({"application/json"})
    public Response getAllTratte() throws Exception {
		Response response=null;
		PreparedStatement query = null;
		Connection conn = null;
		String type=null; String partenza=null; String arrivo=null;String name=null;int id=0;
		List<Tratta> tratte = new ArrayList<Tratta>();
		try {
			conn = Pg.pgConn();
			query = conn.prepareStatement("select * from tratta");
			ResultSet rs = query.executeQuery();
			while(rs.next()) {
				id = rs.getInt("id");
				name = rs.getString("trattaname");
				type = rs.getString("type");
				partenza = rs.getString("partenza");
				arrivo = rs.getString("arrivo");
				PreparedStatement q = conn.prepareStatement("select id,slotname,slots,day,inizio,fine from slotPerTratta join slot on slotPerTratta.slot = slot.id where tratta='"+id+"'");
				ResultSet result = q.executeQuery();
				List<Slot> slotTratta= new ArrayList<Slot>();
				while(result.next()) {
					int slotId = Integer.parseInt(result.getString("id"));
					int slots = Integer.parseInt(result.getString("slots"));
					String nameSlot = result.getString("slotname");
					String day = result.getString("day");
					String inizio = result.getString("inizio");
					String fine = result.getString("fine");
					PreparedStatement QUERY = conn.prepareStatement("select aereo from aereiPerSlot where slot='"+slotId+"'");
					ResultSet risultato = QUERY.executeQuery();
					List<Integer> accepted_planes=new ArrayList<Integer>();
					
					while(risultato.next()) {
						accepted_planes.add(Integer.parseInt(risultato.getString("aereo")));
					}
					slotTratta.add(new Slot(slotId,nameSlot,slots,day,inizio,fine,accepted_planes));
				}
				tratte.add(new Tratta(id, name, type, partenza, slotTratta,arrivo) );
				q.close();
			}
			query.close();
		} catch(Exception e) {
			e.printStackTrace();
			response=Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}finally {
			if(conn!=null){
				response=Response.ok(tratteToJson(tratte)).build();
				//response=Response.ok(slotTratta.toString()).build();
				conn.close();
			}
		}
		return response;
    }
	@POST
	@Consumes({"application/json"})
	@Produces({"text/plain"})
	public Response createTratta(@Context HttpHeaders headers, String is) throws Exception{
		Response response=null;
		if(!(ImplUtils.isAdmin(headers))) {
			return response = Response.status(Response.Status.UNAUTHORIZED).build();
		}
		Tratta tratta = readTratta(false,is);
		int id = 0;
		String name=tratta.getName();
		String type=tratta.getType();
		String from=tratta.getPartenza();
		String to=tratta.getArrivo();
		List<Integer> slots = tratta.getSlot();
		PreparedStatement query = null;
		Connection conn = null;
		try {
			conn = Pg.pgConn();
			String q = "BEGIN; insert into tratta(trattaname,type,partenza,arrivo) values('"+name+"','"+type+"','"+from+"','"+to+"');";
			query = conn.prepareStatement(q);
			query.executeUpdate();
			q="select max(id) from tratta;";
			query = conn.prepareStatement(q);
			ResultSet rs = query.executeQuery();
			while(rs.next()) {
				id=rs.getInt("max");
			}
			for(int i=0;i<slots.size();i++) {
				q ="insert into slotPerTratta values('"+id+"','"+slots.get(i)+"');";
				PreparedStatement QUERY = conn.prepareStatement(q);
				QUERY.executeUpdate();
				QUERY.close();
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
			return response = Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}finally {
			if(!conn.isClosed()) {
				String q = "COMMIT;";
				PreparedStatement commit = conn.prepareStatement(q);
				commit.executeUpdate();
				response = Response.created(URI.create("tratta/" + id)).build();
				commit.close();
				conn.close();
			}
		}
		return response;
	}/*
	@POST
	public Response createCustomer(String is) {
		Person person = readPerson(is);
		String id = readId(is);
        PersonDao.instance.getModel().put(id, person);
		return Response.created(URI.create("person/" + id)).build();
	}
	*/
	
	public String tratteToJson(List<Tratta> tratteList) {
		String result ="[";
		int i=1;
		for(Tratta tratta:tratteList) {
			result+=tratta.toJson();
			if(i++ != tratteList.size()) {
				result+=",";
			}
		}
		result+="]";
		return result;
	}
}
	

