package it.synclab.smartparking.resources;

import java.util.ArrayList;
import java.util.List;

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
		logger.info("ParkingResource - END getSensorData");
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
		logger.info("ParkingResource - END saveSensorData");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	// Now this is useless cause we're writing ParkingArea using Sensor's db writer
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
		logger.info("ParkingResource - END saveParkingSensorData");
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
		logger.info("ParkingResource - END getSensorData");
		return ResponseEntity.status(HttpStatus.OK).body(s);
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
		logger.info("ParkingResource - END getSensorState");
		return ResponseEntity.status(HttpStatus.OK).body(sensorState);
	}

	@GetMapping("/sensor/name/{name}")
	@ResponseBody
	public ResponseEntity<Object> getSensorsByName(@PathVariable String name) {
		List<Sensor> l = new ArrayList<>();
		logger.info("ParkingResource - START getSensorsByName");
		try {
			l = parkingService.getSensorsByName(name);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getSensorsByName");
		return ResponseEntity.status(HttpStatus.OK).body(l);
	}

	@GetMapping("/sensor/name/starting-with/{str}")
	@ResponseBody
	public ResponseEntity<Object> getSensorByNameStartingWith(@PathVariable String str) {
		List<Sensor> sensors = new ArrayList<>();
		logger.info("ParkingResource - START getSensorByNameStartingWith");
		try {
			sensors = parkingService.getSensorByNameStartingWith(str);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getSensorByNameStartingWith");
		return ResponseEntity.status(HttpStatus.OK).body(sensors);
	}

	@GetMapping("/sensor/name/ending-with/{str}")
	@ResponseBody
	public ResponseEntity<Object> getSensorByNameEndingWith(@PathVariable String str) {
		List<Sensor> sensors = new ArrayList<>();
		logger.info("ParkingResource - START getSensorByNameEndingWith");
		try {
			sensors = parkingService.getSensorByNameEndingWith(str);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getSensorByNameEndingWith");
		return ResponseEntity.status(HttpStatus.OK).body(sensors);
	}
	
	@GetMapping("/sensor/name/containing/{str}")
	@ResponseBody
	public ResponseEntity<Object> getSensorByNameContaining(@PathVariable String str) {
		List<Sensor> sensors = new ArrayList<>();
		logger.info("ParkingResource - START getSensorByNameContaining");
		try {
			sensors = parkingService.getSensorByNameContaining(str);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getSensorByNameContaining");
		return ResponseEntity.status(HttpStatus.OK).body(sensors);
	}
	
	@GetMapping("/sensor/isActive")
	@ResponseBody
	public ResponseEntity<Object> getSensorByIsActiveTrue() {
		List<Sensor> sensors = new ArrayList<>();
		logger.info("ParkingResource - START getSensorByIsActiveTrue");
		try {
			sensors = parkingService.getSensorByIsActiveTrue();
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getSensorByIsActiveTrue");
		return ResponseEntity.status(HttpStatus.OK).body(sensors);
	}
	
	@GetMapping("/sensor/notActive")
	@ResponseBody
	public ResponseEntity<Object> getSensorByIsActiveFalse() {
		List<Sensor> sensors = new ArrayList<>();
		logger.info("ParkingResource - START getSensorByIsActiveFalse");
		try {
			sensors = parkingService.getSensorByIsActiveFalse();
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getSensorByIsActiveFalse");
		return ResponseEntity.status(HttpStatus.OK).body(sensors);
	}

	@PutMapping("/sensor/update/name/{sensorId}")
	public ResponseEntity<Object> updateSensorNameById(@RequestBody String name, @PathVariable Long sensorId) {
		logger.info("ParkingResource - START updateSensorNameById");
		try {
			parkingService.updateSensorNameById(name, sensorId);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END updateSensorNameById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PutMapping("/sensor/update/type/{sensorId}")
	public ResponseEntity<Object> updateSensorTypeById(@RequestBody String type, @PathVariable Long sensorId) {
		logger.info("ParkingResource - START updateSensorTypeById");
		try {
			parkingService.updateSensorTypeById(type, sensorId);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END updateSensorTypeById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/sensor/delete/{sensorId}")
	public ResponseEntity<Object> deleteSensorById(@PathVariable Long sensorId) {
		logger.info("ParkingResource - START deleteSensorById");
		try {
			parkingService.deleteSensorById(sensorId);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END deleteSensorById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/sensor/delete/all")
	public ResponseEntity<Object> deleteSensors() {
		logger.info("ParkingResource - START deleteSensors");
		try {
			parkingService.deleteAlSensors();
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END deleteSensors");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}