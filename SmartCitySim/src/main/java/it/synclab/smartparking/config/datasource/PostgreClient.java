package it.synclab.smartparking.config.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

public class PostgreClient {

	Properties props = new Properties();
	@Value("${postgres.jdbc.url}")
	private String jdbcURL;

	@Value("${postgres.username}")
	private String username;

	@Value("${postgres.password}")
	private String password;

	private Connection conn = null;

	private static final Logger logger = LogManager.getLogger(PostgreClient.class);

	public PostgreClient() {
		logger.info("PostgreClient START - trying connection to PosgresDB");
		try {
			conn = DriverManager.getConnection(jdbcURL,username,password);
		} catch (SQLException e) {
			logger.error("PostgreClient Error", e);
		}
		logger.info("PostgreClient END - connection to PosgresDB Success");
	}

}
