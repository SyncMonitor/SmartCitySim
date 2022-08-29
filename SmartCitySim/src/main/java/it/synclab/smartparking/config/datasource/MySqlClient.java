package it.synclab.smartparking.config.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Value;


public class MySqlClient {

	@Value("${mysql.jdbc.url}")
	private String jdbcURL;
	
	@Value("${mysql.username}")
	private String username;
	
	@Value("${mysql.password}")
	private String password;
	
	private Connection conn = null;

	private static final Logger logger = LogManager.getLogger(MySqlClient.class);;

	public MySqlClient(){
		try {
			logger.debug("MySqlClient - START - trying connection to MySqlDB");
			conn = DriverManager.getConnection(jdbcURL, username, password);
		} catch (SQLException e) {
			logger.error("MySqlClient - ERROR - ", e);
		}
		logger.debug("MySqlClient - END - connection to MySqlDB Success");
	}
	
}
