package it.synclab.smartparking.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.synclab.smartparking.repository.model.Sensor;

@Repository
public interface SensorsRepository extends CrudRepository<Sensor, Long> {
	
	public List<Sensor> findByName(String name);
	
	
	//JPQL query based on Java class, not on DB tables.
	@Query("select s from Sensor s where s.id = ?1")
	Sensor getById(int sensorId);

}
