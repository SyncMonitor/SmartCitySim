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

	// JPQL query based on Java class, not on DB tables.
	@Query("select s from Sensor s where s.id = ?1")
	Sensor getSensorById(Long sensorId);

	@Query("select s.isActive from Sensor s where s.id = ?1")
	public boolean getSensorState(Long sensorId);
	
	@Query("select s from Sensor s where s.name like ?1%")
	public List<Sensor> getSensorByNameStartingWith(String str);

	@Query("select s from Sensor s where s.name like %?1")
	public List<Sensor> getSensorByNameEndingWith(String str);
	
	@Query("select s from Sensor s where s.name like %?1%")
	public List<Sensor> getSensorByNameContaining(String str);
	
	public List<Sensor> getSensorsByName(String name);
	
	public List<Sensor> getSensorsByType(String type);
	
	public List<Sensor> getSensorByIsActiveTrue();
	
	public List<Sensor> getSensorByIsActiveFalse();
		
	//Update

	@Modifying
	@Query("update Sensor c set c.name = ?1 where id = ?2")
	public void updateSensorName(String name, Long sensorId);
	
	@Modifying
	@Query("update Sensor c set c.battery = ?1 where id = ?2")
	public void updateSensorBattery(String battery, Long sensorId);
	
	
	@Modifying
	@Query("update Sensor c set c.type = ?1 where id = ?2")
	public void updateSensorTypeById(String type, Long sensorId);
	
	@Modifying
	@Query("update Sensor c set c.isActive = ?1 where id = ?2")
	public void updateStateById(boolean state, Long id);
	
	//Delete

	@Modifying
	@Query("delete Sensor c where id = ?1")
	public void deleteSensorById(Long sensorId);


}
