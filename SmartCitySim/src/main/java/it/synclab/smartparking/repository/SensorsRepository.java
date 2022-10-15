package it.synclab.smartparking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.synclab.smartparking.repository.model.Sensors;

@Transactional
@Repository
public interface SensorsRepository extends CrudRepository<Sensors, Long> {
	
	// JPQL query based on Java class, not on DB tables.
	@Query("select s from Sensors s where s.id = ?1 order by id")
	public Sensors getSensorById(Long sensorId);
	
	@Query("select s from Sensors s order by id")
	public List<Sensors> getAllSensorFromDB();

	@Query("select s.isActive from Sensors s where s.id = ?1")
	public boolean getSensorState(Long sensorId);
	
	@Query("select s from Sensors s where s.name like ?1%")
	public List<Sensors> getSensorByNameStartingWith(String str);

	@Query("select s from Sensors s where s.name like %?1")
	public List<Sensors> getSensorByNameEndingWith(String str);
	
	@Query("select s from Sensors s where s.name like %?1%")
	public List<Sensors> getSensorsByNameContaining(String str);	
	
	public List<Sensors> getSensorsByName(String name);
	
	public List<Sensors> getSensorsByType(String type);
	
	public List<Sensors> getSensorsByIsActiveTrue();
	
	public List<Sensors> getSensorsByIsActiveFalse();
		
	//Update

	@Modifying
	@Query("update Sensors c set c.name = ?1 where id = ?2")
	public void updateSensorsName(String name, Long sensorId);
	
	@Modifying
	@Query("update Sensors c set c.battery = ?1 where id = ?2")
	public void updateSensorsBattery(String battery, Long sensorId);
	
	
	@Modifying
	@Query("update Sensors c set c.type = ?1 where id = ?2")
	public void updateSensorTypeById(String type, Long sensorId);
	
	@Modifying
	@Query("update Sensors c set c.isActive = ?1 where id = ?2")
	public void updateStateById(boolean state, Long id);
	
	@Modifying
	@Query("update Sensors c set c.charge = ?1 where id = ?2")
	public void updateSensorChargeById(String charge, Long sensorId);

	@Modifying
	@Query("update Sensors c set c.lastSurvey = ?1 where id = ?2")
	public void updateLastSurveyById(LocalDateTime date, Long id);
	
	//Delete

	@Modifying
	@Query("delete Sensors c where id = ?1")
	public void deleteSensorById(Long sensorId);

}
