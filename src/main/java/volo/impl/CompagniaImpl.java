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

import volo.dao.CompagniaDao;
import volo.entity.Aereo;
import volo.entity.Compagnia;
import volo.entity.Pg;
import volo.entity.Tratta;
@Path("/compagnia")
public class CompagniaImpl implements CompagniaDao{
	@GET
	@Path("{id}")
	@Produces({"application/json"})
    public Response getCompagnia(@Context HttpHeaders headers, @PathParam("id") String is) throws Exception {
		int id = Integer.parseInt(is);
		Response response=null;
		if(!(ImplUtils.isAuthorized(headers)||ImplUtils.isAdmin(headers))) {
			return response = Response.status(Response.Status.UNAUTHORIZED).build();
		}
		PreparedStatement query = null;
		Connection conn = null;
		String nome=null;String dataIscrizione=null;String nazione=null;double valoreAzionario=0;String password=null;
		List<Aereo> flotta= new ArrayList<Aereo>();
		List<String> partners= new ArrayList<String>();
		try {
			conn = Pg.pgConn();
			query = conn.prepareStatement("select * from compagnia where id="+id+"");
			ResultSet rs = query.executeQuery();
			while(rs.next()) {
				nome = rs.getString("nomecompagnia");
				password= rs.getString("password");
				nazione = rs.getString("nazione");
				dataIscrizione = rs.getString("dataiscrizione");
				valoreAzionario = Double.parseDouble(rs.getString("valoreazionario"));
			}
			PreparedStatement q = conn.prepareStatement("select aereo.* from aereicompagnie join aereo on aereicompagnie.aereo=aereo.id where compagnia="+id+"");
			ResultSet result = q.executeQuery();
			
			while(result.next()) {
				int aereoId = Integer.parseInt(result.getString("id"));
				String nomeAereo = result.getString("nomeaereo");
				String motore = result.getString("motore");
				double lunghezza = Double.parseDouble(result.getString("lunghezza"));
				double aperturaAlare = Double.parseDouble(result.getString("aperturaAlare"));
				double superficieAlare = Double.parseDouble(result.getString("superficieAlare"));
				double capCombustibile = Double.parseDouble(result.getString("capacitacombustibile"));
				int vel = Integer.parseInt(result.getString("vel"));
				int velmax = Integer.parseInt(result.getString("velmax"));
				int pesovuoto = Integer.parseInt(result.getString("pesovuoto"));
				int pesomax = Integer.parseInt(result.getString("pesomax"));
				String classe1 = result.getString("primclasse");
				String classe2 = result.getString("secclasse");
				String classe3 = result.getString("terclasse");
				List<Integer> passeggeri = new ArrayList<Integer>();
				if(classe1!=null) {
					passeggeri.add(Integer.parseInt(classe1));
				}
				if(classe2!=null) {
					passeggeri.add(Integer.parseInt(classe2));
				}
				if(classe3!=null) {
					passeggeri.add(Integer.parseInt(classe3));
				}
				flotta.add(new Aereo(aereoId,nomeAereo,lunghezza,aperturaAlare,vel,velmax,superficieAlare,pesovuoto,pesomax,capCombustibile,motore,passeggeri));
				//;
			}
			q=conn.prepareStatement("select nomepartner from partner where compagnia="+id+";");
			result=q.executeQuery();
			while(result.next()) {
				partners.add(result.getString("nomepartner"));
			}
			q.close();
			query.close();
		} catch(Exception e) {
			e.printStackTrace();
			response=Response.status(Response.Status.NOT_FOUND).build();
		}finally {
			if(!conn.isClosed()){
				Compagnia compagnia = new Compagnia(flotta,id,password,nome,dataIscrizione,nazione,valoreAzionario,partners);
				response=Response.ok(compagnia.toJson()).build();
				//response=Response.ok(slotTratta.toString()).build();
				conn.close();
			}
		}
		return response;
    }
	@GET
	@Path("all")
	@Produces({"application/json"})
    public Response getAllCompagnie(@Context HttpHeaders headers) throws Exception {
		Response response=null;
		if(!ImplUtils.isAdmin(headers)) {
			return response = Response.status(Response.Status.UNAUTHORIZED).build();
		}
		PreparedStatement query = null;
		Connection conn = null;
		String nome=null;String dataIscrizione=null;String nazione=null;double valoreAzionario=0;String password=null;int compagniaId=0;
		
		
		List<Compagnia> compagniaList = new ArrayList<>();
		try {
			conn = Pg.pgConn();
			query = conn.prepareStatement("select * from compagnia ");
			ResultSet rs = query.executeQuery();
			while(rs.next()) {
				compagniaId = Integer.parseInt(rs.getString("id"));
				nome = rs.getString("nomecompagnia");
				password= rs.getString("password");
				nazione = rs.getString("nazione");
				dataIscrizione = rs.getString("dataiscrizione");
				valoreAzionario = Double.parseDouble(rs.getString("valoreazionario"));
				PreparedStatement q = conn.prepareStatement("select aereo.* from aereicompagnie join aereo on aereicompagnie.aereo=aereo.id where compagnia="+compagniaId+"");
				ResultSet result = q.executeQuery();
				List<String> partners= new ArrayList<String>();
				List<Aereo> flotta= new ArrayList<Aereo>();
				while(result.next()) {
					int aereoId = Integer.parseInt(result.getString("id"));
					String nomeAereo = result.getString("nomeaereo");
					String motore = result.getString("motore");
					double lunghezza = Double.parseDouble(result.getString("lunghezza"));
					double aperturaAlare = Double.parseDouble(result.getString("aperturaAlare"));
					double superficieAlare = Double.parseDouble(result.getString("superficieAlare"));
					double capCombustibile = Double.parseDouble(result.getString("capacitacombustibile"));
					int vel = Integer.parseInt(result.getString("vel"));
					int velmax = Integer.parseInt(result.getString("velmax"));
					int pesovuoto = Integer.parseInt(result.getString("pesovuoto"));
					int pesomax = Integer.parseInt(result.getString("pesomax"));
					String classe1 = result.getString("primclasse");
					String classe2 = result.getString("secclasse");
					String classe3 = result.getString("terclasse");
					List<Integer> passeggeri = new ArrayList<Integer>();
					if(classe1!=null) {
						passeggeri.add(Integer.parseInt(classe1));
					}
					if(classe2!=null) {
						passeggeri.add(Integer.parseInt(classe2));
					}
					if(classe3!=null) {
						passeggeri.add(Integer.parseInt(classe3));
					}
					flotta.add(new Aereo(aereoId,nomeAereo,lunghezza,aperturaAlare,vel,velmax,superficieAlare,pesovuoto,pesomax,capCombustibile,motore,passeggeri));
					//;
				}
				q=conn.prepareStatement("select nomepartner from partner where compagnia="+compagniaId+";");
				result=q.executeQuery();
				while(result.next()) {
					partners.add(result.getString("nomepartner"));
				}
				compagniaList.add(new Compagnia(flotta,compagniaId,password,nome,dataIscrizione,nazione,valoreAzionario,partners));
				q.close();
			}
			
			query.close();
		} catch(Exception e) {
			e.printStackTrace();
			response=Response.status(Response.Status.NOT_FOUND).build();
		}finally {
			if(!conn.isClosed()){
				
				response=Response.ok(compagnieToJson(compagniaList)).build();
				//response=Response.ok(slotTratta.toString()).build();
				conn.close();
			}
		}
		return response;
    }
	@DELETE
	@Path("{id}")
	@Produces({"application/json"})
    public Response deleteCompagnia(@Context HttpHeaders headers, @PathParam("id") String is) throws Exception{
		int id =Integer.parseInt(is);
		Response response=null;
		if(!(ImplUtils.isAuthorized(headers)||ImplUtils.isAdmin(headers))) {
			return response = Response.status(Response.Status.UNAUTHORIZED).build();
		}
		PreparedStatement query = null;
		Connection conn = null;
		try {
			conn = Pg.pgConn();
			String q = "BEGIN;";
			query = conn.prepareStatement(q);
			query.executeUpdate();
			q = "select count(*) from compagnia where id="+id+";";
			query = conn.prepareStatement(q);
			ResultSet rs = query.executeQuery();
			int rowcount = 0;
			while(rs.next()) {
				rowcount=rs.getInt("count");
			}
			if(rowcount==0) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			q = "delete from compagnia where id="+id+";";
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
	@POST
	@Path("register")
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response registerCompagnia(String is) throws Exception{
		Response response=null;
		Compagnia compagnia = readCompagniaRegistrazione(is);
		int id = 0;
		String nome= compagnia.getNome();
		String password=compagnia.getPassword();
		String nazione = compagnia.getNazione();
		String dataIscrizione = compagnia.getDataIscrizione();
		double valAzionario=compagnia.getValoreAzionario();
		PreparedStatement query = null;
		Connection conn = null;
		try {
			conn = Pg.pgConn();
			String q = "BEGIN; insert into compagnia(nomecompagnia,password,dataiscrizione,nazione,valoreazionario) values('"+nome+"','"+password+"','"+dataIscrizione+"','"+nazione+"',"+valAzionario+");";
			query = conn.prepareStatement(q);
			query.executeUpdate();
			q="select max(id) from compagnia;";
			query = conn.prepareStatement(q);
			ResultSet rs = query.executeQuery();
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
			return response = Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}finally {
			if(!conn.isClosed()) {
				String q = "COMMIT;";
				PreparedStatement commit = conn.prepareStatement(q);
				commit.executeUpdate();
				response = Response.created(URI.create("compagnia/" + id)).build();
				commit.close();
				conn.close();
			}
		}
		return response;
	}
	@POST
	@Path("login")
	public Response loginCompagnia(@Context HttpHeaders headers) throws Exception{
		Response response =null;
		if(!(ImplUtils.isAuthorized(headers)||ImplUtils.isAdmin(headers))) {
			response = Response.status(Response.Status.FORBIDDEN).build();
		}else {
			response = Response.status(Response.Status.OK).build();
		}
		return response;
	}
	@PUT
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response updateCompagnia(@Context HttpHeaders headers, String is) throws Exception{
		Response response=null;
		if(!(ImplUtils.isAuthorized(headers)||ImplUtils.isAdmin(headers))) {
			return response = Response.status(Response.Status.UNAUTHORIZED).build();
		}
		Compagnia compagnia = readCompagnia(false,is);
		int compagniaId = compagnia.getId();
		String nome= compagnia.getNome();
		String password=compagnia.getPassword();
		String nazione = compagnia.getNazione();
		double valAzionario=compagnia.getValoreAzionario();
		List<Integer> flotta = compagnia.getAerei();
		List<String> partners = compagnia.getPartner();
		PreparedStatement query = null;
		Connection conn = null;
		try {
			conn = Pg.pgConn();
			
			String q="BEGIN;";
			query = conn.prepareStatement(q);
			query.executeUpdate();
			q = "select count(*) from compagnia where id="+compagniaId+";";
			query = conn.prepareStatement(q);
			ResultSet rs = query.executeQuery();
			int rowcount = 0;
			while(rs.next()) {
				rowcount=rs.getInt("count");
			}
			if(rowcount==0) {
				return Response.status(Response.Status.NOT_FOUND).build();
			} 
			
			q = "update compagnia set nomecompagnia='"+nome+"',password='"+password+"',nazione='"+nazione+"',valoreazionario="+valAzionario+" where id="+compagniaId+";";
			query = conn.prepareStatement(q);
			query.executeUpdate();
			q = "delete from aereiCompagnie where compagnia="+compagniaId+";";
			query = conn.prepareStatement(q);
			query.executeUpdate();
			q = "delete from partner where compagnia="+compagniaId+";";
			query = conn.prepareStatement(q);
			query.executeUpdate();
			for(int i=0;i<flotta.size();i++) {
				q ="insert into aereiCompagnie values("+compagniaId+","+flotta.get(i)+");";
				PreparedStatement QUERY = conn.prepareStatement(q);
				QUERY.executeUpdate();
				QUERY.close();
			}
			for(int i=0;i<partners.size();i++) {
				q ="insert into partner values('"+partners.get(i).replaceAll("\"","")+"',"+compagniaId+");";
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
				response = Response.ok("compagnia/" + compagniaId).build();
				commit.close();
				conn.close();
			}
		}
		return response;
	}
	private Compagnia readCompagnia(boolean passBool,String s) {
		JsonReader jsonReader = Json.createReader(new StringReader(s));
		JsonObject o = jsonReader.readObject();
		String password=null;
		if(passBool==true) {
			password = o.getString("password");
		}
		int id =o.getInt("id");
		String nome = o.getString("name");
		String dataIscrizione = o.getString("dataIscrizione");
		String nazione = o.getString("nazione");
		double valAzionario = o.getJsonNumber("valoreAzionario").doubleValue();
		JsonArray flotta = o.getJsonArray("flotta");
		JsonArray partner = o.getJsonArray("partner");
		List<JsonValue> listFlotta = (List<JsonValue>) flotta;
		List<Integer> aerei = new ArrayList<Integer>();
		for(int i=0;i<listFlotta.size();i++) {
			aerei.add(Integer.parseInt(listFlotta.get(i).toString()));
		}
		List<JsonValue> lpartner = (List<JsonValue>) partner;
		List<String> Partner = new ArrayList<String>();
		for(int i=0;i<lpartner.size();i++) {
			Partner.add(lpartner.get(i).toString());
		}
		Compagnia compagnia = null;
		if(passBool==true) {
			compagnia = new Compagnia(id,nome,password,dataIscrizione,nazione,valAzionario,aerei,Partner);
		}else {
			compagnia = new Compagnia(id,nome,dataIscrizione,nazione,valAzionario,aerei,Partner);
		}	
		return compagnia;
	}
	private Compagnia readCompagniaRegistrazione(String s) {
		JsonReader jsonReader = Json.createReader(new StringReader(s));
		JsonObject o = jsonReader.readObject();
		String nome = o.getString("name");
		String password = o.getString("password");
		String dataIscrizione = o.getString("dataIscrizione");
		String nazione = o.getString("nazione");
		double valAzionario = o.getJsonNumber("valoreAzionario").doubleValue();
		return new Compagnia(nome,password,dataIscrizione,nazione,valAzionario);
	}
	private String compagnieToJson(List<Compagnia> compagnie) {
		String result ="[";
		int i=1;
		for(Compagnia compagnia:compagnie) {
			result+=compagnia.toJson();
			if(i++ != compagnie.size()) {
				result+=",";
			}
		}
		result+="]";
		return result;
	}
}
	
