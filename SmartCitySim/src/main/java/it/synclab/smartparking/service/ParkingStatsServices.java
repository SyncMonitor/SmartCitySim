package it.synclab.smartparking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.synclab.smartparking.repository.ParkingAreaStatsRepository;
import it.synclab.smartparking.repository.model.ParkingArea;
import it.synclab.smartparking.repository.model.ParkingAreaStats;

@Component
@Transactional
public class ParkingStatsServices {

	@Value("${sensor.parking.url}")
	private String sensorDataUrl;


	@Autowired
	private ParkingAreaStatsRepository parkingAreaStatsRepository;
	
	
	private static final Logger logger = LogManager.getLogger(ParkingStatsServices.class);
	
	public ParkingAreaStats buildParkingAreaStatsFromParkingArea(ParkingArea parkArea) {
		logger.debug("ParkingService START buildParkingAreaStatsFromParkingArea");
		ParkingAreaStats stats = new ParkingAreaStats();
		if (parkArea.getFkSensorId() != null) {
			stats.setFkSensorId(parkArea.getFkSensorId());
		}
		if (parkArea.getLastUpdate() != null) {
			stats.setLastUpdate(parkArea.getLastUpdate());
		}
		logger.debug("ParkingService END buildParkingAreaStatsFromParkingArea");
		stats.setValue(parkArea.getValue());
		return stats;
	}

	public void saveParkingAreaStats(ParkingAreaStats stats) {
		logger.debug("ParkingService START saveParkingAreaStats");
		try {
			parkingAreaStatsRepository.save(stats);
		} catch (Exception e) {
			logger.error("ParkingService - Error", e);
		}
		logger.debug("ParkingService END saveParkingAreaStats");
	}

	public List<ParkingAreaStats> getParkingAreaStats() {
		logger.debug("ParkingService START getParkingAreaStats");
		List<ParkingAreaStats> stats = parkingAreaStatsRepository.getParkingAreaStats();
		logger.debug("ParkingService END getParkingAreaStats");
		return stats;
	}

	public ParkingAreaStats getParkingAreaStatsById(Long id) {
		logger.debug("ParkingService START getParkingAreaStatsBySensorId");
		ParkingAreaStats stat = parkingAreaStatsRepository.getParkingAreaStatsById(id);
		logger.debug("ParkingService END getParkingAreaStatsBySensorId");
		return stat;
	}

	public List<ParkingAreaStats> getParkingAreaStatsBySensorId(Long sensorId) {
		logger.debug("ParkingService START getParkingAreaStatsBySensorId");
		List<ParkingAreaStats> stats = parkingAreaStatsRepository.getParkingAreaStatsBySensorId(sensorId);
		logger.debug("ParkingService END getParkingAreaStatsBySensorId");
		return stats;
	}

	public List<ParkingAreaStats> getParkingAreaStatsFromDate(LocalDateTime date) {
		logger.debug("ParkingService START getParkingAreaStatsFromData");
		List<ParkingAreaStats> stats = parkingAreaStatsRepository.getParkingAreaStatsFromData(date);
		logger.debug("ParkingService END getParkingAreaStatsFromData");
		return stats;
	}

	public List<ParkingAreaStats> getParkingAreaStatsFromDateToDate(LocalDateTime startData, LocalDateTime endData) {
		logger.debug("ParkingService START getParkingAreaStatsFromDateToDate");
		List<ParkingAreaStats> stats = parkingAreaStatsRepository.getParkingAreaStatsFromDateToDate(startData, endData);
		logger.debug("ParkingService END getParkingAreaStatsFromDateToDate");
		return stats;
	}

	public List<ParkingAreaStats> getParkingAreaStatsBySensorIdFromDate(Long id, LocalDateTime data) {
		logger.debug("ParkingService START getParkingAreaStatsBySensorIdFromData");
		List<ParkingAreaStats> stats = parkingAreaStatsRepository.getParkingAreaStatsBySensorIdFromData(id, data);
		logger.debug("ParkingService END getParkingAreaStatsBySensorIdFromData");
		return stats;
	}

	public List<ParkingAreaStats> getParkingAreaStatsBySensorIdFromDateToDate(Long id, LocalDateTime startData,
			LocalDateTime endData) {
		logger.debug("ParkingService START getParkingAreaStatsBySensorIdFromDataToData");
		List<ParkingAreaStats> stats = parkingAreaStatsRepository.getParkingAreaStatsBySensorIdFromDataToData(id,
				startData, endData);
		logger.debug("ParkingService END getParkingAreaStatsBySensorIdFromDataToData");
		return stats;
	}

	public void deleteParkingAreaStatsBeforeDate(LocalDateTime data) {
		logger.debug("ParkingService START deleteParkingAreaStatsBeforeDate");
		parkingAreaStatsRepository.deleteParkingAreaStatsBeforeDate(data);
		logger.debug("ParkingService END deleteParkingAreaStatsBeforeDate");
	}

	public void deleteParkingAreaStatsById(Long id) {
		logger.debug("ParkingService START deleteParkingAreaStatsById");
		parkingAreaStatsRepository.deleteParkingAreaStatsById(id);
		logger.debug("ParkingService END deleteParkingAreaStatsById");
	}

	public void deleteParkingAreaStatsBySensorId(Long sensorId) {
		logger.debug("ParkingService START deleteParkingAreaStatsBySensorId");
		parkingAreaStatsRepository.deleteParkingAreaStatsBySensorId(sensorId);
		logger.debug("ParkingService END deleteParkingAreaStatsBySensorId");
	}

	public void deleteAllParkingStats() {
		logger.debug("ParkingService START deleteAlSensors ");
		parkingAreaStatsRepository.deleteAll();
		logger.debug("ParkingService END deleteAlSensors ");
	}
	
}
