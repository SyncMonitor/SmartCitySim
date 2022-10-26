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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.synclab.smartparking.model.Maintainer;
import it.synclab.smartparking.repository.model.MaintainersRegistry;
import it.synclab.smartparking.service.SensorMaintainerServices;


@RestController
@RequestMapping("/scs")
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SensorMaintainerResources {

	private static final Logger logger = LogManager.getLogger(SensorMaintainerResources.class);	
	@Autowired
	private SensorMaintainerServices sensorsMaintainerServices;

	@GetMapping("/maintainer/sensor/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> getSensorsMaintainersBySensorId(@PathVariable Long sensorId) {
		logger.info("SensorMaintainerResources - START getSensorsMaintainersBySensorId");
		List<MaintainersRegistry> mainteiners = null;
		try {
			mainteiners = sensorsMaintainerServices.getSensorsMaintainerDataBySensorId(sensorId);
		} catch (NullPointerException e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorMaintainerResources - END getSensorsMaintainersBySensorId");
		return ResponseEntity.status(HttpStatus.OK).body(mainteiners);

	}

	@GetMapping("/maintainer/{id}")
	@ResponseBody
	public ResponseEntity<Object> getSensorsMaintainersById(@PathVariable Long id) {
		logger.info("SensorMaintainerResources - START getSensorsMaintainersBySensorId");
		MaintainersRegistry mainteiner = null;
		try {
			mainteiner = sensorsMaintainerServices.getSensorsMaintainerDataById(id);
		} catch (NullPointerException e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorMaintainerResources - END getSensorsMaintainersBySensorId");
		return ResponseEntity.status(HttpStatus.OK).body(mainteiner);

	}

	@GetMapping("/maintainers/all")
	@ResponseBody
	public ResponseEntity<Object> getAllSensorsMaintainersData() {
		logger.info("SensorMaintainerResources - START getAllSensorsMaintainersData");
		List<MaintainersRegistry> mainteiners = new ArrayList<>();
		try {
			mainteiners = sensorsMaintainerServices.getAllSensorsMaintainerData();
		} catch (Exception e) {
			logger.error("SensorMaintainerResources -  error - getAllSensorsMaintainersData", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorMaintainerResources - END getAllSensorsMaintainersData");
		return ResponseEntity.status(HttpStatus.OK).body(mainteiners);

	}

	@PutMapping("/update/maintainer/sensor/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> updateMaintainerBySensorId(@PathVariable Long sensorId,
			@RequestBody Maintainer maintainer) {
		logger.info("SensorMaintainerResources - START updateMaintainerBySensorId");
		try {
			sensorsMaintainerServices.updateSensorsMaintainerDataBySensorId(maintainer, sensorId);
		} catch (NullPointerException e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorMaintainerResources - END updateMaintainerBySensorId");
		return ResponseEntity.status(HttpStatus.OK).build();

	}

	@PutMapping("/update/maintainer/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateMaintainerById(@PathVariable Long id, @RequestBody Maintainer maintainer) {
		logger.info("SensorMaintainerResources - START updateMaintainerById");
		try {
			sensorsMaintainerServices.updateSensorsMaintainerDataById(maintainer, id);
		} catch (NullPointerException e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorMaintainerResources - END updateMaintainerById");
		return ResponseEntity.status(HttpStatus.OK).build();

	}

	@PutMapping("/update/maintainer/to-be-repaired/true/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> updateSensorMaintainerToBeRepairedTrueBySensorId(@PathVariable Long sensorId) {
		logger.info("SensorMaintainerResources - START updateSensorMaintainerToBeRepairedTrueBySensorId");
		try {
			sensorsMaintainerServices.updateSensorMaintainerToBeRepairedBySensorId(true, sensorId);
		} catch (NullPointerException e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorMaintainerResources - END updateSensorMaintainerToBeRepairedTrueBySensorId");
		return ResponseEntity.status(HttpStatus.OK).build();

	}

	@PutMapping("/update/maintainer/to-be-repaired/false/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> updateSensorMaintainerToBeRepairedFalseBySensorId(@PathVariable Long sensorId) {
		logger.info("SensorMaintainerResources - START updateSensorMaintainerToBeRepairedFalseBySensorId");
		try {
			sensorsMaintainerServices.updateSensorMaintainerToBeRepairedBySensorId(false, sensorId);
		} catch (NullPointerException e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorMaintainerResources - END updateSensorMaintainerToBeRepairedFalseBySensorId");
		return ResponseEntity.status(HttpStatus.OK).build();

	}

	@PutMapping("/update/maintainer/to-be-charged/true/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> updateSensorMaintainerToBeChargedTrueBySensorId(@PathVariable Long sensorId) {
		logger.info("SensorMaintainerResources - START updateSensorMaintainerToBeChargedTrueBySensorId");
		try {
			sensorsMaintainerServices.updateSensorMaintainerToBeChargedBySensorId(true, sensorId);
		} catch (NullPointerException e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorMaintainerResources - END updateSensorMaintainerToBeChargedTrueBySensorId");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/update/maintainer/to-be-charged/false/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> updateSensorMaintainerToBeChargedFalseBySensorId(@PathVariable Long sensorId) {
		logger.info("SensorMaintainerResources - START updateSensorMaintainerToBeChargedFalseBySensorId");
		try {
			sensorsMaintainerServices.updateSensorMaintainerToBeChargedBySensorId(false, sensorId);
		} catch (Exception e) {
			logger.error("SensorMaintainerResources -  error - updateSensorMaintainerToBeChargedFalseBySensorId", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("SensorMaintainerResources - END updateSensorMaintainerToBeChargedFalseBySensorId");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("sensor/maintainers/delete/{sensorId}")
	public ResponseEntity<Object> deleteSensorMaintainersBySensorId(@PathVariable Long sensorId) {
		logger.info("SensorMaintainerResources - START deleteSensorMaintainersBySensorId");
		try {
			sensorsMaintainerServices.deleteSensorMaintainersBySensorId(sensorId);
		} catch (NullPointerException e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorMaintainerResources - END deleteSensorMaintainersBySensorId");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/maintainers/delete/{id}")
	public ResponseEntity<Object> deleteSensorMaintainersById(@PathVariable Long id) {
		logger.info("SensorMaintainerResources - START deleteSensorMaintainersById");
		try {
			sensorsMaintainerServices.deleteSensorMaintainersById(id);
		} catch (NullPointerException e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("SensorMaintainerResources -  error - getSensorsMaintainersBySensorId", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorMaintainerResources - END deleteSensorMaintainersById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/maintainers/delete/all")
	public ResponseEntity<Object> deleteAllSensorMaintainers() {
		logger.info("SensorMaintainerResources - START deleteAllSensorMaintainers");
		try {
			sensorsMaintainerServices.deleteAllSensorMaintainers();
		} catch (Exception e) {
			logger.error("SensorMaintainerResources -  error - deleteAllSensorMaintainers", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("SensorMaintainerResources - END deleteAllSensorMaintainers");
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}