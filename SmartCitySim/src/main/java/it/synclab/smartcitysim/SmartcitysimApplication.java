package it.synclab.smartcitysim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartcitysimApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartcitysimApplication.class, args);
	}

}
