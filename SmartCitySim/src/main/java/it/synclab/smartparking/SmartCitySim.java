package it.synclab.smartparking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"it.synclab"})
public class SmartCitySim {

	public static void main(String[] args) {
		SpringApplication.run(SmartCitySim.class, args);
		
	}
}