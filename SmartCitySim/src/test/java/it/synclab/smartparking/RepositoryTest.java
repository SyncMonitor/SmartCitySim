package it.synclab.smartparking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import it.synclab.smartcity.jparepository.SensorsRepository;
import it.synclab.smartparking.repository.model.Sensors;


@DataJpaTest
public class RepositoryTest {

	
	private SensorsRepository sensorsRepository;

	void contextLoads() {
	}

	 @Autowired
	  private TestEntityManager entityManager;
	
	@Test
	public void saveSensors() {
		try {
			Sensors sensor = new Sensors("Name", "Battery", "OtherProperty", true);
			//sensorsRepository.save(sensor);
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}

	}

}