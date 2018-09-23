package volo.heroku_volo;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;

public class ImplUtils {
	protected static boolean isAuthorized(HttpHeaders headers) throws SQLException, UnsupportedEncodingException {
		String authHeader = headers.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
		String[] parts = authHeader.split(" ");
		byte[] decoded = Base64.decodeBase64(parts[1]);
		String rawDecoded = new String(decoded, "UTF-8");
		String[] credentials = rawDecoded.split(":");
		String compagnia = credentials[0];
		String password = credentials[1];
		Response response=null;
		Connection conn = null;
		try {
			conn = Pg.pgConn();
			String q = "SELECT count(*) from compagnia where nomecompagnia='"+compagnia+"' and password='"+password+"'";
			PreparedStatement query = conn.prepareStatement(q);
			ResultSet rs = query.executeQuery();
			int rowcount = 0;
			while(rs.next()) {
				rowcount=rs.getInt("count");
			}
			if(rowcount==0) {
				return false;
			}else if(rowcount==1){
				return true;
			}
			query.close();
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
			conn.close();
			return false;
		}
		return false;
	}
	protected static boolean isAdmin(HttpHeaders headers) throws SQLException, UnsupportedEncodingException {
		String authHeader = headers.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
		String[] parts = authHeader.split(" ");
		byte[] decoded = Base64.decodeBase64(parts[1]);
		String rawDecoded = new String(decoded, "UTF-8");
		String[] credentials = rawDecoded.split(":");
		String username = credentials[0];
		String password = credentials[1];
		Connection conn = null;
		try {
			conn = Pg.pgConn();
			String q = "SELECT count(*) from admin where username='"+username+"' and password='"+password+"'";
			PreparedStatement query = conn.prepareStatement(q);
			ResultSet rs = query.executeQuery();
			int rowcount = 0;
			while(rs.next()) {
				rowcount=rs.getInt("count");
			}
			if(rowcount==0) {
				return false;
			}else if(rowcount==1){
				return true;
			}
			query.close();
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
			conn.close();
			return false;
		}
		return false;
	}
}
