package it.synclab.smartparking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.synclab.smartparking.repository.model.ParkingArea;

@Transactional
@Repository
public interface ParkingAreaRepository extends CrudRepository<ParkingArea, Long> {

	public ParkingArea getParkingAreaById(Long id);
	
	public ParkingArea getParkingAreaByFkSensorId(Long sensorId);

	@Query("select p.latitude from ParkingArea p where p.id = ?1")
	public String getLatitudeById(Long id);

	@Query("select p.longitude from ParkingArea p where p.id = ?1")
	public String getLongitudeById(Long id);

	@Query("select p.address from ParkingArea p where p.id = ?1")
	public String getAddressById(Long id);

	@Query("select p.value from ParkingArea p where p.id = ?1")
	public boolean getStateById(Long id);

	@Query("select p from ParkingArea p where p.value = true")
	public List<ParkingArea> getParkingAreaByValueTrue();
	
	@Query("select p from ParkingArea p where p.value = false")
	public List<ParkingArea> getParkingAreaByValueFalse();
	
	public ParkingArea getParkingAreaByLatitudeAndLongitude(String latitude, String longitude);

	@Query("select p.lastUpdate from ParkingArea p where p.fkSensorId = ?1")
	public LocalDateTime getLastUpdateBySensoId(Long sensorId);
	
	@Modifying
	@Query("update ParkingArea p set p.latitude = ?1 where id = ?2")
	public void updateLatitudeById(String latitude, Long id);

	@Modifying
	@Query("update ParkingArea p set p.longitude = ?1 where id = ?2")
	public void updateLongitudeById(String longitude, Long id);

	@Modifying
	@Query("update ParkingArea p set p.address = ?1 where id = ?2")
	public void updateAddressById(String address, Long id);

	@Modifying
	@Query("update ParkingArea p set p.value = ?1 where id = ?2")
	public void updateValueById(boolean state, Long id);
	
	@Modifying
	@Query("update ParkingArea p set p.lastUpdate = ?1 where fkSensorId = ?2")
	public void updateSensorDateBySensorId(LocalDateTime data, Long sensorId);


	@Modifying
	@Query("delete ParkingArea p where p.id = ?1")
	public void deleteParkingAreaById(Long id);
	
	@Modifying
	@Query("delete ParkingArea p where p.fkSensorId = ?1")
	public void deleteParkingAreaBySensorId(Long sensorId);

}
