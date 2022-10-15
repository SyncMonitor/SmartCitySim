package it.synclab.smartparking.resources;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.AopInvocationException;
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

import it.synclab.smartparking.model.MarkerList;
import it.synclab.smartparking.repository.model.Sensors;
import it.synclab.smartparking.service.SensorsServices;
import it.synclab.smartparking.service.StartUpServices;

@RestController
@RequestMapping("/scs")
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SensorsResources {

	private static final Logger logger = LogManager.getLogger(SensorsResources.class);

	@Autowired
	private StartUpServices startUpServices;
	
	@Autowired
	private SensorsServices sensorService;
	

	@GetMapping("/sensor/get-all-data")
	@ResponseBody
	public ResponseEntity<Object> getSensorsData() {
		logger.info("SensorResources - START getSensorData");
		List<Sensors> list = null;
		// Security user check
		try {
			list = sensorService.getAllSensorsFromDB();
		} catch (Exception e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END getSensorData");
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@GetMapping("/sensor/get-data-from-XML")
	@ResponseBody
	public ResponseEntity<Object> getSensorsDataFromSource() {
		logger.info("SensorResources - START getSensorsDataFromSource");
		MarkerList list = null;
		// Security user check
		try {
			list = startUpServices.readDataFromSources();
		} catch (Exception e) {
			logger.error("SensorResources -  error - getSensorsDataFromSource", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END getSensorsDataFromSource");
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@PostMapping("/sensor/save-data")
	@ResponseBody
	public ResponseEntity<Object> saveSensorData() {
		logger.info("SensorResources - START saveSensorData");
		// Security user check
		try {
			startUpServices.writeSensorsData();
		} catch (Exception e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END saveSensorData");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/sensor/data/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> getSensorData(@PathVariable Long sensorId) {
		logger.info("SensorResources - START getSensorData");
		Sensors s;
		// Security user check
		try {
			s = sensorService.getSensorById(sensorId);
		} catch (NullPointerException e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} 
		catch (Exception e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END getSensorData");
		return ResponseEntity.status(HttpStatus.OK).body(s);
	}

	@GetMapping("/sensor/state/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> getSensorState(@PathVariable Long sensorId) {
		logger.info("SensorResources - START getSensorState");
		boolean sensorState = false;
		// Security user check
		try {
			sensorState = sensorService.getSensorState(sensorId);
		} catch (AopInvocationException e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} 
		catch (Exception e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END getSensorState");
		return ResponseEntity.status(HttpStatus.OK).body(sensorState);
	}

	@GetMapping("/sensor/name/{name}")
	@ResponseBody
	public ResponseEntity<Object> getSensorsByName(@PathVariable String name) {
		List<Sensors> l = new ArrayList<>();
		logger.info("SensorResources - START getSensorsByName");
		try {
			l = sensorService.getSensorsByName(name);
		} catch (NullPointerException e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} 
		catch (Exception e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END getSensorsByName");
		return ResponseEntity.status(HttpStatus.OK).body(l);
	}

	@GetMapping("/sensor/type/{type}")
	@ResponseBody
	public ResponseEntity<Object> getSensorsBytype(@PathVariable String type) {
		List<Sensors> l = new ArrayList<>();
		logger.info("SensorResources - START getSensorsByType");
		try {
			l = sensorService.getSensorsByType(type);
		} catch (NullPointerException e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} 
		catch (Exception e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END getSensorsByType");
		return ResponseEntity.status(HttpStatus.OK).body(l);
	}

	@GetMapping("/sensor/name/starting-with/{str}")
	@ResponseBody
	public ResponseEntity<Object> getSensorByNameStartingWith(@PathVariable String str) {
		List<Sensors> sensors = new ArrayList<>();
		logger.info("SensorResources - START getSensorByNameStartingWith");
		try {
			sensors = sensorService.getSensorByNameStartingWith(str);
		} catch (NullPointerException e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} 
		catch (Exception e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END getSensorByNameStartingWith");
		return ResponseEntity.status(HttpStatus.OK).body(sensors);
	}

	@GetMapping("/sensor/name/ending-with/{str}")
	@ResponseBody
	public ResponseEntity<Object> getSensorByNameEndingWith(@PathVariable String str) {
		List<Sensors> sensors = new ArrayList<>();
		logger.info("SensorResources - START getSensorByNameEndingWith");
		try {
			sensors = sensorService.getSensorByNameEndingWith(str);
		} catch (NullPointerException e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} 
		catch (Exception e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END getSensorByNameEndingWith");
		return ResponseEntity.status(HttpStatus.OK).body(sensors);
	}

	@GetMapping("/sensor/name/containing/{str}")
	@ResponseBody
	public ResponseEntity<Object> getSensorByNameContaining(@PathVariable String str) {
		List<Sensors> sensors = new ArrayList<>();
		logger.info("SensorResources - START getSensorByNameContaining");
		try {
			sensors = sensorService.getSensorByNameContaining(str);
		} catch (NullPointerException e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} 
		catch (Exception e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END getSensorByNameContaining");
		return ResponseEntity.status(HttpStatus.OK).body(sensors);
	}

	@GetMapping("/sensor/isActive")
	@ResponseBody
	public ResponseEntity<Object> getSensorByIsActiveTrue() {
		List<Sensors> sensors = new ArrayList<>();
		logger.info("SensorResources - START getSensorByIsActiveTrue");
		try {
			sensors = sensorService.getSensorByIsActiveTrue();
		} catch (NullPointerException e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} 
		catch (Exception e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END getSensorByIsActiveTrue");
		return ResponseEntity.status(HttpStatus.OK).body(sensors);
	}

	@GetMapping("/sensor/notActive")
	@ResponseBody
	public ResponseEntity<Object> getSensorByIsActiveFalse() {
		List<Sensors> sensors = new ArrayList<>();
		logger.info("SensorResources - START getSensorByIsActiveFalse");
		try {
			sensors = sensorService.getSensorByIsActiveFalse();
		} catch (NullPointerException e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} 
		catch (Exception e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END getSensorByIsActiveFalse");
		return ResponseEntity.status(HttpStatus.OK).body(sensors);
	}

	@PutMapping("/sensor/update/name/{sensorId}")
	public ResponseEntity<Object> updateSensorNameById(@RequestBody String name, @PathVariable Long sensorId) {
		logger.info("SensorResources - START updateSensorNameById");
		try {
			sensorService.updateSensorNameById(name, sensorId);
		} catch (NullPointerException e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} 
		catch (Exception e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END updateSensorNameById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/sensor/update/battery/{sensorId}")
	public ResponseEntity<Object> updateSensorBatteryById(@RequestBody String battery, @PathVariable Long sensorId) {
		logger.info("SensorResources - START updateSensorBatteryById");
		try {
			sensorService.updateSensorBatteryById(battery, sensorId);
		} catch (NullPointerException e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} 
		catch (Exception e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END updateSensorBatteryById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/sensor/update/type/{sensorId}")
	public ResponseEntity<Object> updateSensorTypeById(@RequestBody String type, @PathVariable Long sensorId) {
		logger.info("SensorResources - START updateSensorTypeById");
		try {
			sensorService.updateSensorTypeById(type, sensorId);
		} catch (NullPointerException e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} 
		catch (Exception e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END updateSensorTypeById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/sensor/update/state/{state}/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateSensorStateById(@PathVariable boolean state, @PathVariable Long id) {
		logger.info("SensorResources - START updateSensorStateById");
		try {
			sensorService.updateSensorStateById(state, id);
		} catch (NullPointerException e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} 
		catch (Exception e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END updateSensorStateById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/sensor/delete/{sensorId}")
	public ResponseEntity<Object> deleteSensorById(@PathVariable Long sensorId) {
		logger.info("SensorResources - START deleteSensorById");
		try {
			sensorService.deleteSensorById(sensorId);
		} catch (NullPointerException e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} 
		catch (Exception e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END deleteSensorById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/sensor/delete/all")
	public ResponseEntity<Object> deleteSensors() {
		logger.info("SensorResources - START deleteSensors");
		try {
			sensorService.deleteAllSensors();
		} catch (Exception e) {
			logger.error("SensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorResources - END deleteSensors");
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}