package it.synclab.smartparking;


import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.synclab.smartparking.repository.SensorsRepository;
import it.synclab.smartparking.repository.model.Sensor;

//@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(
		  locations = "classpath:application-integrationtest.properties")
@ExtendWith(SpringExtension.class)
public class RepositoryTest {

	@Autowired
	private SensorsRepository sensorsRepository;
	
//	@Test
//	public void saveSensors() {
//		try {
//			Sensor sensor = new Sensor("Name", "Battery", "OtherProperty", true);
//			sensorsRepository.save(sensor);
//		} catch (Exception e) {
//			System.out.println("Error: " + e);
//		}
//
//	}

}