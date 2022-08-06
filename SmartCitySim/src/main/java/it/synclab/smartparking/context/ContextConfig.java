package it.synclab.smartparking.context;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import it.synclab.smartparking.datasource.config.PostgreClient;
import it.synclab.smartparking.service.ParkingService;

@Configuration
public class ContextConfig {


  
	/*
	 * Uncomment this if you want to use PostgerSQL DB
	 * 
	 * 
	@Bean
	public PostgreClient createPostgresClient() {
		return new PostgreClient();
	}*/

	@Bean
	public DataSource datasource() {
		return DataSourceBuilder.create().driverClassName("com.mysql.cj.jdbc.Driver")
				.url("jdbc:mysql://localhost:3306/smartcity").username("root").password("password").build();
	}
	
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	    mailSender.setUsername("smartcitysimulator@gmail.com");
	    mailSender.setPassword("wbyzeelawblrotls");
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    return mailSender;
	}
	
}
