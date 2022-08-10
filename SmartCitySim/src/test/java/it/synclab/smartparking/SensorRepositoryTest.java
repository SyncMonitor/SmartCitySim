package it.synclab.smartparking;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.synclab.smartparking.repository.SensorsRepository;
import it.synclab.smartparking.repository.model.Sensor;
import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartCitySim.class)
public class SensorRepositoryTest {

	@Autowired
	private SensorsRepository sensorsRepository;

	Sensor sensor = null;

	@Before
	public void init() {
		sensor = new Sensor();
		sensor.setId(109L);
		sensor.setName("Sensor 109");
		sensor.setBattery("3,3V");
		sensor.setCharge("3");
		sensor.setType("parkingArea");
		sensor.setActive(true);
	}

	@Test
	public void saveSensor() {
		try {

			sensorsRepository.save(sensor);
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}

	}
//
//	@Test
//	public void getSensorByIdTest(Long id) {
//		
//
//	}

}