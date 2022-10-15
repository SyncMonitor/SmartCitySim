package it.synclab.smartparking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.synclab.smartparking.repository.model.ParkingSensors;
import it.synclab.smartparking.repository.model.Sensors;

@Transactional
@Repository
public interface ParkingSensorsRepository extends CrudRepository<ParkingSensors, Long> {
	
	@Query("select p from ParkingArea p order by id")
	public List<ParkingSensors> getAllParkingSensors();
	
	public ParkingSensors getParkingSensorsById(Long id);
	
	@Query("select p.latitude from ParkingSensors p where p.sensors.id = ?1")
	public ParkingSensors getParkingSensorsBySensorId(Long sensorId);
	
	@Query("select p.latitude from ParkingSensors p where p.id = ?1")
	public String getLatitudeById(Long id);

	@Query("select p.longitude from ParkingSensors p where p.id = ?1")
	public String getLongitudeById(Long id);

	@Query("select p.address from ParkingSensors p where p.id = ?1")
	public String getAddressById(Long id);

	@Query("select p.value from ParkingSensors p where p.id = ?1")
	public boolean getStateById(Long id);

	@Query("select p from ParkingSensors p where p.value = true")
	public List<ParkingSensors> getParkingSensorsByValueTrue();
	
	@Query("select p from ParkingSensors p where p.value = false")
	public List<ParkingSensors> getParkingSensorsByValueFalse();
	
	public ParkingSensors getParkingSensorsByLatitudeAndLongitude(String latitude, String longitude);

	@Query("select p.timestamp from ParkingSensors p where p.sensors.id = ?1")
	public LocalDateTime getTimestampBySensorId(Long sensorId);
	
	@Modifying
	@Query("update ParkingSensors p set p.latitude = ?1 where id = ?2")
	public void updateLatitudeById(String latitude, Long id);

	@Modifying
	@Query("update ParkingSensors p set p.longitude = ?1 where id = ?2")
	public void updateLongitudeById(String longitude, Long id);

	@Modifying
	@Query("update ParkingSensors p set p.address = ?1 where id = ?2")
	public void updateAddressById(String address, Long id);

	@Modifying
	@Query("update ParkingSensors p set p.value = ?1 where id = ?2")
	public void updateValueById(boolean state, Long id);
	
	@Modifying
	@Query("update ParkingSensors p set p.timestamp = ?1 where p.sensors.id = ?2")
	public void updateTimestampBySensorsId(LocalDateTime data, Long sensorId);

	@Modifying
	@Query("delete ParkingSensors p where p.id = ?1")
	public void deleteParkingSensorsById(Long id);
	
	//@Modifying
	//@Query("delete ParkingSensors p where p.fkSensorId = ?1")
	public void deleteParkingSensorsBySensorsId(Long sensorId);
}
