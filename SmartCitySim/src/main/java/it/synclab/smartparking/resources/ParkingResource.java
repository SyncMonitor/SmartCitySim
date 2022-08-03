package it.synclab.smartparking.resources;

import java.time.LocalDateTime;
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

import it.synclab.smartparking.model.Maintainer;
import it.synclab.smartparking.model.MarkerList;
import it.synclab.smartparking.repository.model.ParkingArea;
import it.synclab.smartparking.repository.model.Sensor;
import it.synclab.smartparking.repository.model.SensorsMaintainer;
import it.synclab.smartparking.service.ParkingService;

@RestController
@RequestMapping("/scs")
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ParkingResource {

	private static final Logger logger = LogManager.getLogger(ParkingResource.class);

	@Autowired
	private ParkingService parkingService;
	
//	@Autowired
//	private MailService mailService;

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

//	Now this is useless cause we're writing ParkingArea using Sensor's db writer
//	@PostMapping("/parking/save-data")
//	@ResponseBody
//	public ResponseEntity<Object> saveParkingSensorData() {
//		logger.info("ParkingResource - START saveParkingSensorData");
//		// Security user check
//		try {
//			parkingService.saveParkingSensorData();
//		} catch (Exception e) {
//			logger.error("ParkingResource -  error", e);
//			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//		}
//		logger.info("ParkingResource - END saveParkingSensorData");
//		return ResponseEntity.status(HttpStatus.OK).build();
//	}

	@GetMapping("/sensor/data/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> getSensorData(@PathVariable Long sensorId) {
		logger.info("ParkingResource - START getSensorData");
		Sensor s;
		// Security user check
		try {
			s = parkingService.getSensorById(sensorId);
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

	@GetMapping("/sensor/type/{type}")
	@ResponseBody
	public ResponseEntity<Object> getSensorsBytype(@PathVariable String type) {
		List<Sensor> l = new ArrayList<>();
		logger.info("ParkingResource - START getSensorsByType");
		try {
			l = parkingService.getSensorsByType(type);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getSensorsByType");
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
	
	@PutMapping("/sensor/update/battery/{sensorId}")
	public ResponseEntity<Object> updateSensorBatteryById(@RequestBody String battery, @PathVariable Long sensorId) {
		logger.info("ParkingResource - START updateSensorBatteryById");
		try {
			parkingService.updateSensorBatteryById(battery, sensorId);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END updateSensorBatteryById");
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
	
	@PutMapping("/sensor/update/state/{state}/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateSensorStateById(@PathVariable boolean state ,@PathVariable Long id) {
		logger.info("ParkingResource - START updateSensorStateById");
		try {
			parkingService.updateSensorStateById(state, id);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END updateSensorStateById");
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

// ParkingArea

	@GetMapping("/parking-area/data/{id}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaData(@PathVariable Long id) {
		logger.info("ParkingResource - START getParkingAreaData");
		ParkingArea p;
		// Security user check
		try {
			p = parkingService.getParkingAreaById(id);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getParkingAreaData");
		return ResponseEntity.status(HttpStatus.OK).body(p);
	}
	
	@GetMapping("/parking-area/data-from-sensor/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaDataBySensorId(@PathVariable Long sensorId) {
		logger.info("ParkingResource - START getParkingAreaDataBySensorId");
		ParkingArea p;
		// Security user check
		try {
			p = parkingService.getParkingAreaBySensorId(sensorId);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getParkingAreaDataBySensorId");
		return ResponseEntity.status(HttpStatus.OK).body(p);
	}
	
	@GetMapping("/parking-area/latitude/{id}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaLatitudeById(@PathVariable Long id) {
		logger.info("ParkingResource - START getParkingAreaLatitudeById");
		String latitude;
		// Security user check
		try {
			latitude = parkingService.getParkingAreaLatitudeById(id);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getParkingAreaLatitudeById");
		return ResponseEntity.status(HttpStatus.OK).body(latitude);
	}
	
	@GetMapping("/parking-area/longitude/{id}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaLongitudeById(@PathVariable Long id) {
		logger.info("ParkingResource - START getParkingAreaLongitudeById");
		String latitude;
		// Security user check
		try {
			latitude = parkingService.getParkingAreaLongitudeById(id);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getParkingAreaLongitudeById");
		return ResponseEntity.status(HttpStatus.OK).body(latitude);
	}
	
	@GetMapping("/parking-area/address/{id}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaAddressById(@PathVariable Long id) {
		logger.info("ParkingResource - START getParkingAreaLongitudeById");
		String address;
		// Security user check
		try {
			address = parkingService.getParkingAreaAddressById(id);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getParkingAreaAddressById");
		return ResponseEntity.status(HttpStatus.OK).body(address);
	}
	
	@GetMapping("/parking-area/state/{id}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaStateById(@PathVariable Long id) {
		logger.info("ParkingResource - START getParkingAreaStateById");
		boolean state;
		// Security user check
		try {
			state = parkingService.getParkingAreaStateById(id);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getParkingAreaStateById");
		return ResponseEntity.status(HttpStatus.OK).body(state);
	}
	
	@GetMapping("/parking-area/free")
	@ResponseBody
	public ResponseEntity<Object> getFreeParkingArea() {
		logger.info("ParkingResource - START getFreeParkingArea");
		List<ParkingArea> state;
		// Security user check
		try {
			state = parkingService.getFreeParkingArea();
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getFreeParkingArea");
		return ResponseEntity.status(HttpStatus.OK).body(state);
	}
	
	@GetMapping("/parking-area/occupy")
	@ResponseBody
	public ResponseEntity<Object> getOccupyParkingArea() {
		logger.info("ParkingResource - START getOccupyParkingArea");
		List<ParkingArea> state;
		// Security user check
		try {
			state = parkingService.getOccupyParkingArea();
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getOccupyParkingArea");
		return ResponseEntity.status(HttpStatus.OK).body(state);
	}

	@GetMapping("/parking-area/{latitude}/{longitude}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaByLatitudeAndLongitude(@PathVariable String latitude, @PathVariable String longitude) {
		logger.info("ParkingResource - START getParkingAreaByLatitudeAndLongitude");
		ParkingArea parkArea;
		// Security user check
		try {
			parkArea = parkingService.getParkingAreaByLatitudeAndLongitude(latitude,longitude);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getParkingAreaByLatitudeAndLongitude");
		return ResponseEntity.status(HttpStatus.OK).body(parkArea);
	}
	
	@GetMapping("/parking-area/last-update-date/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaLastUpdateDateBySensorId(@PathVariable Long sensorId) {
		logger.info("ParkingResource - START getParkingAreaLastUpdateDateBySensorId");
		LocalDateTime date;
		// Security user check
		try {
			date = parkingService.getParkingAreaLastUpdateDateBySensorId(sensorId);
		} catch (Exception e) {
			logger.error("ParkingResource -  ERROR - getParkingAreaLastUpdateDateBySensorId", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getParkingAreaLastUpdateDateBySensorId");
		return ResponseEntity.status(HttpStatus.OK).body(date);
	}
	
	@PutMapping("/parking-area/update/latitude/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateParkingAreaLatitudeById(@PathVariable Long id, @RequestBody String latitude) {
		logger.info("ParkingResource - START updateParkingAreaLatitudeById");
		try {
			parkingService.updateParkingAreaLatitudeById(latitude, id);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END updateParkingAreaLatitudeById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PutMapping("/parking-area/update/longitude/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateParkingAreaLongitudeById(@PathVariable Long id, @RequestBody String longitude) {
		logger.info("ParkingResource - START updateParkingAreaLatitudeById");
		try {
			parkingService.updateParkingAreaLongitudeById(longitude, id);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END updateParkingAreaLongitudeById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	
	@PutMapping("/parking-area/update/address/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateParkingAreaAddressById(@PathVariable Long id, @RequestBody String address) {
		logger.info("ParkingResource - START updateParkingAreaAddressById");
		try {
			parkingService.updateParkingAreaAddressById(address, id);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END updateParkingAreaAddressById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PutMapping("/parking-area/update/state/{state}/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateParkingAreaStateById(@PathVariable boolean state ,@PathVariable Long id) {
		logger.info("ParkingResource - START updateParkingAreaStateById");
		try {
			parkingService.updateParkingAreaValueById(state, id);
		} catch (Exception e) {
			logger.error("ParkingResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END updateParkingAreaStateById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
//	SensorsMaintainer's Service
	
	@GetMapping("/maintainer/sensor/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> getSensorsMaintainersBySensorId(@PathVariable Long sensorId) {
		logger.info("ParkingResource - START getSensorsMaintainersBySensorId");
		List<SensorsMaintainer> mainteiners = null;
		try {
			mainteiners = parkingService.getSensorsMaintainerDataBySensorId(sensorId);
		} catch (Exception e) {
			logger.error("ParkingResource -  error - sendMail", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getSensorsMaintainersBySensorId");
		return ResponseEntity.status(HttpStatus.OK).body(mainteiners);
	
	}
	
	@GetMapping("/maintainer/{id}")
	@ResponseBody
	public ResponseEntity<Object> getSensorsMaintainersById(@PathVariable Long id) {
		logger.info("ParkingResource - START getSensorsMaintainersBySensorId");
		SensorsMaintainer mainteiner = null;
		try {
			mainteiner = parkingService.getSensorsMaintainerDataById(id);
		} catch (Exception e) {
			logger.error("ParkingResource -  error - sendMail", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END getSensorsMaintainersBySensorId");
		return ResponseEntity.status(HttpStatus.OK).body(mainteiner);
	
	}
	
	@PutMapping("/update/maintainer/sensor/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> updateMaintainerBySensorId(@PathVariable Long sensorId, @RequestBody Maintainer maintainer) {
		logger.info("ParkingResource - START updateMaintainerBySensorId");
		try {
			parkingService.updateSensorsMaintainerData(maintainer, sensorId);
		} catch (Exception e) {
			logger.error("ParkingResource -  error - updateMaintainerById", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END updateMaintainerBySensorId");
		return ResponseEntity.status(HttpStatus.OK).build();
	
	}
	
	@PutMapping("/update/maintainer/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateMaintainerById(@PathVariable Long id, @RequestBody Maintainer maintainer) {
		logger.info("ParkingResource - START updateMaintainerById");
		try {
			parkingService.updateSensorsMaintainerDataById(maintainer, id);
		} catch (Exception e) {
			logger.error("ParkingResource -  error - updateMaintainerById", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END updateMaintainerById");
		return ResponseEntity.status(HttpStatus.OK).build();
	
	}
	
	@PutMapping("/update/maintainer/to-be-repaired/true/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> updateSensorMaintainerToBeRepairedTrueById(@PathVariable Long sensorId) {
		logger.info("ParkingResource - START updateSensorMaintainerToBeRepairedTrueById");
		try {
			parkingService.updateSensorMaintainerToBeRepairedById(true, sensorId);
		} catch (Exception e) {
			logger.error("ParkingResource -  error - updateMaintainerById", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END updateSensorMaintainerToBeRepairedTrueById");
		return ResponseEntity.status(HttpStatus.OK).build();
	
	}
	
	@PutMapping("/update/maintainer/to-be-repaired/false/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> updateSensorMaintainerToBeRepairedFalseById (@PathVariable Long sensorId) {
		logger.info("ParkingResource - START updateSensorMaintainerToBeRepairedFalseById");
		try {
			parkingService.updateSensorMaintainerToBeRepairedById(false, sensorId);
		} catch (Exception e) {
			logger.error("ParkingResource -  error - updateMaintainerById", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingResource - END updateSensorMaintainerToBeRepairedFalseById");
		return ResponseEntity.status(HttpStatus.OK).build();
	
	}
	
	
}