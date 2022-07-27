package it.synclab.smartparking.context;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.synclab.smartparking.datasource.config.MySqlClient;
import it.synclab.smartparking.datasource.config.PostgreClient;
import it.synclab.smartparking.service.ParkingService;

@Configuration
public class ContextConfig {

	//@Bean
	public ParkingService createParkingService() {
		return new ParkingService();
	}


	@Bean
	public PostgreClient createPostgresClient() {
		return new PostgreClient();
	}
	
//	@Bean
//    public DataSource datasource() {
//        return DataSourceBuilder.create()
//          .driverClassName("com.mysql.cj.jdbc.Driver")
//          .url("jdbc:mysql://localhost:3306/smartcity")
//          .username("lubu")
//          .password("password")
//          .build();	
//    }
}
