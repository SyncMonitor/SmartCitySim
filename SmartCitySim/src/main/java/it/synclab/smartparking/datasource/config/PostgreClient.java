package it.synclab.smartparking.datasource.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreClient {

	
	private String jdbcURL = "jdbc:postgresql://localhost:5432/SmartCitySimulator";
	private String username = "postgres";
	private String password = "password";
	private Connection conn = null;

	public PostgreClient(){
		try {
			conn = DriverManager.getConnection(jdbcURL, username, password);
		} catch (SQLException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	
}
