package k3qKillManager;

import java.sql.*;

public class DbConnection {
	String dbuser;
	String dbpass;
	String db_url;
	Boolean connected = false;
	
	DbConnection (String dbhost, String dbuser, String dbpass, String dbdb) {
		this.dbuser = dbuser;
		this.dbpass = dbpass;
		db_url = "jdbc:mysql://" + dbhost + "/" + dbdb;
	}
	
	
	
	public Connection getConnection() throws SQLException {
		try {
			Connection conn = DriverManager.getConnection(this.db_url, this.dbuser, this.dbpass);
			if (conn != null) {
				
				return conn;
			} else {
				return null;
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
