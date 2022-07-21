package it.synclab.smartparking.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.synclab.smartparking.repository.model.Sensor;

@Transactional
@Repository
public interface SensorsRepository extends CrudRepository<Sensor, Long> {
	
	public List<Sensor> findByName(String name);
	
	
	//JPQL query based on Java class, not on DB tables.
	@Query("select s from Sensor s where s.id = ?1")
	Sensor getSensorById(Long sensorId);

	@Query("select s.isActive from Sensor s where s.id = ?1")
	public boolean getSensorState(Long sensorId);
	
	@Modifying
	@Query("UPDATE Sensor c SET c.name = ?1 where id = ?2")
	public void updateSensorName(String name, Long sensorId);
	
	

}
