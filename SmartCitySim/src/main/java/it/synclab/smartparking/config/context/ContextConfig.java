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

	// @Bean
	public StartUpServices createStartUpServices() {
		return new StartUpServices();
	}
	
	// @Bean
	public SensorServices createPSensorServices() {
		return new SensorServices();
	}
	
	// @Bean
	public ParkingAreaServices createParkingService() {
		return new ParkingAreaServices();
	}
	
	// @Bean
	public SensorMaintainerServices createSensorMaintainerServices() {
		return new SensorMaintainerServices();
	}
	
	// @Bean
	public ParkingStatsServices createParkingStatsServices() {
		return new ParkingStatsServices();
	}
	
	// @Bean
	public MailServices MailServices() {
		return new MailServices();
	}

	// Uncomment this if you want to use PostgerSQL DB
	@Bean
	public PostgreClient createPostgresClient() {
		return new PostgreClient();
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
