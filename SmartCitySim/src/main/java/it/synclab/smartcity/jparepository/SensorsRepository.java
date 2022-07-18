package it.synclab.smartcity.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.synclab.smartparking.repository.model.Sensors;


public interface SensorsRepository extends JpaRepository<Sensors, Long> {

	public Sensors findByName(String name);

}
