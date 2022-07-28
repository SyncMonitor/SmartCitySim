package it.synclab.smartparking;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"it.synclab"})
@EnableScheduling
public class SmartCitySim {

	private static final Logger LOGGER = LogManager.getLogger(SmartCitySim.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SmartCitySim.class, args);
		
		LOGGER.info("info logger");
		LOGGER.debug("debug logger");
		LOGGER.error("error logger");
		
	}
}