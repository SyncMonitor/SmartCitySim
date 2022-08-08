package it.synclab.smartparking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.synclab.smartparking.repository.model.ParkingAreaStats;

public interface ParkingAreaStatsRepository extends CrudRepository<ParkingAreaStats, Long> {
	
	@Query("select s from ParkingAreaStats s order by id desc")
	public List<ParkingAreaStats> getParkingAreaStats();
	
	@Query("select s from ParkingAreaStats s where s.id = ?1 order by id desc")
	public ParkingAreaStats getParkingAreaStatsById(Long id);
	
	@Query("select s from ParkingAreaStats s where s.fkSensorId = ?1 order by id desc")
	public List<ParkingAreaStats> getParkingAreaStatsBySensorId(Long sensorId);
	
	@Query("select s from ParkingAreaStats s where s.lastUpdate >= ?1")
	public List<ParkingAreaStats> getParkingAreaStatsFromData(LocalDateTime data);
	
//	@Query("select s from ParkingAreaStats s where s.lastUpdate > ?1 and s.lastUpdate < ?2")
//	public List<ParkingAreaStats> getParkingAreaStatsFromDataToData(LocalDateTime startDate, LocalDateTime endDate);
	
//	@Query("select s from ParkingAreaStats s where s.fkSensorId = ?1 and lastUpdate > ?2")
//	public List<ParkingAreaStats> getParkingAreaStatsBySensorIdFromData(Long sensorId, LocalDateTime date);
	
//	@Query("select s from ParkingAreaStats s where s.fkSensorId = ?1 and lastUpdate > ?2 and lastUpdate < ?3")
//	public List<ParkingAreaStats> getParkingAreaStatsBySensorIdFromDataToData(Long sensorId, LocalDateTime startDate, LocalDateTime endDate);
	
	
//	//Delete

//	@Modifying
//	@Query("delete ParkingAreaStats s where s.lastUpdate < ?1")
//	public void deleteParkingAreaStatsBeforeDate(LocalDateTime date);
	
	@Modifying
	@Query("delete ParkingAreaStats s where s.id = ?1")
	public void deleteParkingAreaStatsById(Long id);
	
	@Modifying
	@Query("delete ParkingAreaStats s where s.fkSensorId = ?1")
	public void deleteParkingAreaStatsBySensorId(Long sensorId);

}
