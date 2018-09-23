package volo.heroku_volo;


import java.sql.Connection;
import java.sql.DriverManager;


public class Pg {
	private static Connection pg = null;
	public static Connection pgConn(){
		if(pg!=null) {
			return pg;
		}
		try {
			Class.forName("org.postgresql.Driver");
		    String username = "smkvxobvepzgum";
		    String password = "f248eb412d81892b88f454626f5e186f7e76fd1ffd7ea907727a14573577239c";
		    String dbUrl = "jdbc:postgresql://" + "ec2-54-246-101-215.eu-west-1.compute.amazonaws.com" + ":" + "5432" + "/dckq1d825cgf86?sslmode=require" ;

		    return DriverManager.getConnection(dbUrl, username, password);
		    
		} catch(Exception e) {
			e.printStackTrace();
		}
		return pg;
	}
}
