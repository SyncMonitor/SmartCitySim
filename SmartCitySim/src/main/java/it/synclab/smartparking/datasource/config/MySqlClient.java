package it.synclab.smartparking.datasource.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;


public class MySqlClient {

	@Value("${mysql.jdbc.url}")
	private String jdbcURL;
	
	@Value("${mysql.username}")
	private String username;
	
	@Value("${mysql.password}")
	private String password;
	
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
