package it.synclab.smartparking.resources;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.synclab.smartparking.model.SearchDateFilter;
import it.synclab.smartparking.repository.model.ParkingAreaStats;
import it.synclab.smartparking.service.ParkingStatsServices;

@RestController
@RequestMapping("/scs")
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ParkingStatsResources {

	private static final Logger logger = LogManager.getLogger(ParkingStatsResources.class);

	@Autowired
	private ParkingStatsServices parkingStatsService;

	@GetMapping("/parking-stats/")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaStats() {
		logger.info("ParkingStatsResources - START getParkingAreaStats");
		List<ParkingAreaStats> stats;
		try {
			stats = parkingStatsService.getParkingAreaStats();
		} catch (Exception e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStats", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingStatsResources - END getParkingAreaStats");
		return ResponseEntity.status(HttpStatus.OK).body(stats);
	}

	@GetMapping("/parking-stats/{id}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaStatsById(@PathVariable Long id) {
		logger.info("ParkingStatsResources - START getParkingAreaStatsById");
		ParkingAreaStats stat;
		try {
			stat = parkingStatsService.getParkingAreaStatsById(id);
		} catch (NullPointerException e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingStatsResources - END getParkingAreaStatsById");
		return ResponseEntity.status(HttpStatus.OK).body(stat);
	}

	@GetMapping("/sensor/parking-stats/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaStatsBySensorId(@PathVariable Long sensorId) {
		logger.info("ParkingStatsResources - START getParkingAreaStatsBySensorId");
		List<ParkingAreaStats> stats;
		try {
			stats = parkingStatsService.getParkingAreaStatsBySensorId(sensorId);
		} catch (NullPointerException e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingStatsResources - END getParkingAreaStatsBySensorId");
		return ResponseEntity.status(HttpStatus.OK).body(stats);
	}

	@GetMapping("/parking-stats/from/{date}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaStatsFromDate(
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
		logger.info("ParkingStatsResources - START getParkingAreaStatsFromDate");
		List<ParkingAreaStats> stats;
		try {
			stats = parkingStatsService.getParkingAreaStatsFromDate(date);
		} catch (NullPointerException e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingStatsResources - END getParkingAreaStatsFromDate");
		return ResponseEntity.status(HttpStatus.OK).body(stats);
	}

	@GetMapping("/parking-stats/from-date-to-date")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaStatsFromDataToData(@RequestBody SearchDateFilter date) {
		logger.info("ParkingResource - START getParkingAreaStatsFromDataToData");
		List<ParkingAreaStats> stats;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime startDate = LocalDateTime.parse(date.getStartDate(), formatter);
			LocalDateTime endDate = LocalDateTime.parse(date.getEndDate(), formatter);
			stats = parkingStatsService.getParkingAreaStatsFromDateToDate(startDate, endDate);
		} catch (NullPointerException e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingResource - END getParkingAreaStatsFromDataToData");
		return ResponseEntity.status(HttpStatus.OK).body(stats);
	}

	@GetMapping("/sensor/parking-stats/{id}/{data}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaStatsBySensorIdFromData(@PathVariable Long id,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data) {
		logger.info("ParkingStatsResources - START getParkingAreaStatsBySensorIdFromData");
		List<ParkingAreaStats> stats;
		try {
			stats = parkingStatsService.getParkingAreaStatsBySensorIdFromDate(id, data);
		} catch (NullPointerException e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingStatsResources - END getParkingAreaStatsBySensorIdFromData");
		return ResponseEntity.status(HttpStatus.OK).body(stats);
	}

	@GetMapping("/sensor/parking-stats/{id}/{startData}/{endData}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaStatsBySensorIdFromDataToData(@PathVariable Long id,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startData,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endData) {
		logger.info("ParkingStatsResources - START getParkingAreaStatsBySensorIdFromDataToData");
		List<ParkingAreaStats> stats;
		try {
			stats = parkingStatsService.getParkingAreaStatsBySensorIdFromDateToDate(id, startData, endData);
		} catch (NullPointerException e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingStatsResources - END getParkingAreaStatsBySensorIdFromDataToData");
		return ResponseEntity.status(HttpStatus.OK).body(stats);
	}

	@DeleteMapping("/parking-stats/delete/before/{data}")
	public ResponseEntity<Object> deleteParkingAreaStatsBeforeDate(
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data) {
		logger.info("ParkingStatsResources - START deleteParkingAreaStatsBeforeDate");
		try {
			parkingStatsService.deleteParkingAreaStatsBeforeDate(data);
		} catch (NullPointerException e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingStatsResources - END deleteAllParkingAreaStats");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/parking-stats/delete/{id}")
	public ResponseEntity<Object> deleteParkingAreaStatsById(@PathVariable Long id) {
		logger.info("ParkingStatsResources - START deleteParkingAreaStatsById");
		try {
			parkingStatsService.deleteParkingAreaStatsById(id);
		} catch (NullPointerException e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingStatsResources - END deleteParkingAreaStatsById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/sensor/parking-stats/delete/{sensorId}")
	public ResponseEntity<Object> deleteParkingAreaStatsBySensorId(@PathVariable Long sensorId) {
		logger.info("ParkingStatsResources - START deleteParkingAreaStatsBySensorId");
		try {
			parkingStatsService.deleteParkingAreaStatsBySensorId(sensorId);
		} catch (NullPointerException e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingStatsResources -  error - getParkingAreaStatsById", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingStatsResources - END deleteParkingAreaStatsBySensorId");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/parking-stats/delete/all")
	public ResponseEntity<Object> deleteAllParkingstats() {
		logger.info("ParkingStatsResources - START deleteAllParkingstats");
		try {
			parkingStatsService.deleteAllParkingStats();
		} catch (Exception e) {
			logger.error("ParkingStatsResources -  error - deleteAllParkingstats", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingStatsResources - END deleteAllParkingstats");
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}