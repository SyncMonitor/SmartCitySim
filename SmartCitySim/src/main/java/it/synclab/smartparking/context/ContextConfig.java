package it.synclab.smartparking.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.synclab.smartparking.service.ParkingService;

@Configuration
public class ContextConfig {

	@Bean
	public ParkingService createParkingService() {
		return new ParkingService();
	}

}
