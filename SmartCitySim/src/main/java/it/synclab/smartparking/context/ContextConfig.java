package it.synclab.smartparking.context;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import it.synclab.smartparking.datasource.config.PostgreClient;
import it.synclab.smartparking.service.ParkingService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import javax.sql.DataSource;
import it.synclab.smartparking.datasource.config.MySqlClient;

@Configuration
public class ContextConfig {
	
	//@Bean
	public ParkingService createParkingService() {
		return new ParkingService();
	}

//  Uncomment this if you want to use PostgerSQL DB
	@Bean
	public PostgreClient createPostgresClient() {
 		return new PostgreClient();
	}
	
//	Uncomment this if you want to use MySQL DB
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
