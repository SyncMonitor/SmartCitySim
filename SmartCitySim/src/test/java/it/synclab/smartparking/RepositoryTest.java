package it.synclab.smartparking;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import it.synclab.smartparking.repository.SensorsRepository;
import it.synclab.smartparking.repository.model.Sensor;
import it.synclab.smartparking.service.ParkingService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartCitySim.class)
public class RepositoryTest {

	@Autowired
	private SensorsRepository sensorsRepository;
	
	
	@Autowired
	private ParkingService parkingService;
	
	
	
	@Test
	public void saveSensors() {
		try {
			Sensor sensor = new Sensor();
			sensorsRepository.save(sensor);
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}

	}

}