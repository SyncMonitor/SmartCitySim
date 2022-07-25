package it.synclab.smartparking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"it.synclab"})
@EnableScheduling
public class SmartCitySim {

	public static void main(String[] args) {
		SpringApplication.run(SmartCitySim.class, args);
		
	}
}