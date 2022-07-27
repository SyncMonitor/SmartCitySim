package it.synclab.smartparking.datasource.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MySqlClient {

	//To externzalize
	private String jdbcURL = "mysql://localhost:3306/smartcity";
	
	private String username = "lubu";
	
	private String password = "password";
	
	private Connection conn = null;

	public MySqlClient(){
		try {
			conn = DriverManager.getConnection(jdbcURL, username, password);
		} catch (SQLException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	
}
