package it.synclab.smartparking.config.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.synclab.smartparking.config.datasource.PostgreClient;
import it.synclab.smartparking.service.MailServices;
import it.synclab.smartparking.service.ParkingAreaServices;
import it.synclab.smartparking.service.ParkingStatsServices;
import it.synclab.smartparking.service.SensorMaintainerServices;
import it.synclab.smartparking.service.SensorServices;
import it.synclab.smartparking.service.StartUpServices;

@Configuration
public class ContextConfig {

	@Value("${spring.datasource.url}")
	private String jdbcURL;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	// Uncomment this if you want to use PostgerSQL DB
	@Bean
	public PostgreClient createPostgresClient() {
		return new PostgreClient(jdbcURL,username,password);
	}

	// Uncomment this if you want to use MySQL DB
	// @Bean
	// public DataSource datasource() {
	// 	return DataSourceBuilder.create()
	// 			.driverClassName("com.mysql.cj.jdbc.Driver")
	// 			.url("jdbc:mysql://localhost:3306/smartcity")
	// 			.username("lubu")
	// 			.password("password")
	// 			.build();
	// }

}
