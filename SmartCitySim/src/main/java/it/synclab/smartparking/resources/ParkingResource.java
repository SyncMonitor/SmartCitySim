package it.synclab.smartparking.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.synclab.smartparking.service.ParkingService;
import it.synclab.smartparking.model.MarkerList;
import it.synclab.smartparking.repository.model.Sensor;

@RestController
@RequestMapping("/scs")
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ParkingResource {

	private Logger logger = LogManager.getLogger(ParkingResource.class);

	@Autowired
	private ParkingService parkingService;

	@GetMapping("/sensor/get-all-data")
	@ResponseBody
	public ResponseEntity<Object> getSensorsData() {
		logger.info("ParkingResource - START getSensorData");
		MarkerList list = null;
		// Security user check
		try {
			list = parkingService.readSensorData();
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - end getSensorData");
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@PostMapping("/sensor/save-data")
	@ResponseBody
	public ResponseEntity<Object> saveSensorData() {
		logger.info("ParkingResource - START saveSensorData");
		// Security user check
		try {
			parkingService.writeSensorsData();
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - end saveSensorData");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/parking/save-data")
	@ResponseBody
	public ResponseEntity<Object> saveParkingSensorData() {
		logger.info("ParkingResource - START saveParkingSensorData");
		// Security user check
		try {
			parkingService.saveParkingSensorData();
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - end saveParkingSensorData");
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	

	@GetMapping("/sensor/data/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> getSensorData(@PathVariable Long sensorId) {
		logger.info("ParkingResource - START getSensorData");
		Sensor s;
		// Security user check
		try {
			s = parkingService.getSensorsById(sensorId);
			System.out.println(s);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - end getSensorData");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/sensor/state/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> getSensorState(@PathVariable Long sensorId) {
		logger.info("ParkingResource - START getSensorState");
		boolean sensorState = false;
		// Security user check
		try {
			sensorState = parkingService.getSensorState(sensorId);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - end getSensorState");
		return ResponseEntity.status(HttpStatus.OK).body(sensorState);
	}
	
	@PutMapping("/sensor/update-name/{sensorId}")
	public ResponseEntity<Object> updateSensorNameById(@RequestBody String name, @PathVariable Long sensorId) {
		logger.info("ParkingResource - START updateSensorNameById");
		try {
			parkingService.updateSensorNameById(name, sensorId);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - end updateSensorNameById");
		return ResponseEntity.status(HttpStatus.OK).build();
		

	}
	
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Object> deleteSensorById(@PathVariable Long id) {
		return null;
		// TODO

	}
}