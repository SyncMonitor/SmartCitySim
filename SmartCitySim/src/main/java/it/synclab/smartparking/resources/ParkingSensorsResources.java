package it.synclab.smartparking.resources;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.synclab.smartparking.repository.model.ParkingSensors;
import it.synclab.smartparking.service.ParkingSensorsServices;



@RestController
@RequestMapping("/scs")
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ParkingSensorsResources {

	private static final Logger logger = LogManager.getLogger(ParkingSensorsResources.class);
	
	@Autowired
	private ParkingSensorsServices parkingSensorsService;

	@GetMapping("/parking-area/all")
	@ResponseBody
	public ResponseEntity<Object> getAllParkingArea() {
		logger.info("ParkingAreaResources - START getAllParkingArea");
		List<ParkingSensors> parkingAreas;
		// Security user check
		try {
			parkingAreas = parkingSensorsService.getAllParkingSensors();
		} catch (Exception e) {
			logger.error("ParkingAreaResources -  error - getAllParkingArea", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingAreaResources - END getAllParkingArea");
		return ResponseEntity.status(HttpStatus.OK).body(parkingAreas);
	}

	@GetMapping("/parking-area/data/{id}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaData(@PathVariable Long id) {
		logger.info("ParkingAreaResources - START getParkingAreaData");
		ParkingSensors p;
		// Security user check
		try {
			p = parkingSensorsService.getParkingSensorById(id);
		} catch (NullPointerException e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingAreaResources - END getParkingAreaData");
		return ResponseEntity.status(HttpStatus.OK).body(p);
	}

	@GetMapping("/parking-area/data-from-sensor/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaDataBySensorId(@PathVariable Long sensorId) {
		logger.info("ParkingAreaResource - START getParkingAreaDataBySensorId");
		ParkingSensors p;
		// Security user check
		try {
			p = parkingSensorsService.getParkingSensorBySensorId(sensorId);
		} catch (NullPointerException e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingAreaResource - END getParkingAreaDataBySensorId");
		return ResponseEntity.status(HttpStatus.OK).body(p);
	}

	@GetMapping("/parking-area/latitude/{id}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaLatitudeById(@PathVariable Long id) {
		logger.info("ParkingAreaResource - START getParkingAreaLatitudeById");
		String latitude;
		// Security user check
		try {
			latitude = parkingSensorsService.getParkingSensorLatitudeById(id);
		} catch (NullPointerException e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingAreaResource - END getParkingAreaLatitudeById");
		return ResponseEntity.status(HttpStatus.OK).body(latitude);
	}

	@GetMapping("/parking-area/longitude/{id}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaLongitudeById(@PathVariable Long id) {
		logger.info("ParkingAreaResource - START getParkingAreaLongitudeById");
		String longitude;
		// Security user check
		try {
			longitude = parkingSensorsService.getParkingSensorLongitudeById(id);
		} catch (NullPointerException e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingAreaResource - END getParkingAreaLongitudeById");
		return ResponseEntity.status(HttpStatus.OK).body(longitude);
	}

	@GetMapping("/parking-area/address/{id}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaAddressById(@PathVariable Long id) {
		logger.info("ParkingAreaResource - START getParkingAreaLongitudeById");
		String address;
		// Security user check
		try {
			address = parkingSensorsService.getParkingSensorAddressById(id);
		} catch (NullPointerException e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkiAreangResource - END getParkingAreaAddressById");
		return ResponseEntity.status(HttpStatus.OK).body(address);
	}

	@GetMapping("/parking-area/state/{id}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaStateById(@PathVariable Long id) {
		logger.info("ParkiAreangResource - START getParkingAreaStateById");
		boolean state;
		// Security user check
		try {
			state = parkingSensorsService.getParkingSensorStateById(id);
		} catch (AopInvocationException e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkiAreangResource - END getParkingAreaStateById");
		return ResponseEntity.status(HttpStatus.OK).body(state);
	}

	@GetMapping("/parking-area/free")
	@ResponseBody
	public ResponseEntity<Object> getFreeParkingArea() {
		logger.info("ParkiAreangResource - START getFreeParkingArea");
		List<ParkingSensors> state;
		// Security user check
		try {
			state = parkingSensorsService.getFreeParkingSensors();
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkiAreangResource - END getFreeParkingArea");
		return ResponseEntity.status(HttpStatus.OK).body(state);
	}

	@GetMapping("/parking-area/occupy")
	@ResponseBody
	public ResponseEntity<Object> getOccupyParkingArea() {
		logger.info("ParkiAreangResource - START getOccupiedParkingArea");
		List<ParkingSensors> state;
		// Security user check
		try {
			state = parkingSensorsService.getOccupiedParkingSensors();
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkiAreangResource - END getOccupiedParkingArea");
		return ResponseEntity.status(HttpStatus.OK).body(state);
	}

	@GetMapping("/parking-area/{latitude}/{longitude}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaByLatitudeAndLongitude(@PathVariable String latitude,
			@PathVariable String longitude) {
		logger.info("ParkiAreangResource - START getParkingAreaByLatitudeAndLongitude");
		ParkingSensors parkArea;
		// Security user check
		try {
			parkArea = parkingSensorsService.getParkingSensorByLatitudeAndLongitude(latitude, longitude);
		} catch (NullPointerException e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkiAreangResource - END getParkingAreaByLatitudeAndLongitude");
		return ResponseEntity.status(HttpStatus.OK).body(parkArea);
	}

	@GetMapping("/parking-area/last-update-date/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaLastUpdateDateBySensorId(@PathVariable Long sensorId) {
		logger.info("ParkiAreangResource - START getParkingAreaLastUpdateDateBySensorId");
		LocalDateTime date;
		// Security user check
		try {
			date = parkingSensorsService.getParkingSensorLastUpdateDateBySensorId(sensorId);
		} catch (NullPointerException e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkiAreangResource - END getParkingAreaLastUpdateDateBySensorId");
		return ResponseEntity.status(HttpStatus.OK).body(date);
	}

	@PutMapping("/parking-area/update/latitude/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateParkingAreaLatitudeById(@PathVariable Long id, @RequestBody String latitude) {
		logger.info("ParkiAreangResource - START updateParkingAreaLatitudeById");
		try {
			parkingSensorsService.updateParkingSensorLatitudeById(latitude, id);
		} catch (NullPointerException e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkiAreangResource - END updateParkingAreaLatitudeById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/parking-area/update/longitude/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateParkingAreaLongitudeById(@PathVariable Long id, @RequestBody String longitude) {
		logger.info("ParkiAreangResource - START updateParkingAreaLatitudeById");
		try {
			parkingSensorsService.updateParkingSensorLongitudeById(longitude, id);
		} catch (NullPointerException e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkiAreangResource - END updateParkingAreaLongitudeById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/parking-area/update/address/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateParkingAreaAddressById(@PathVariable Long id, @RequestBody String address) {
		logger.info("ParkiAreangResource - START updateParkingAreaAddressById");
		try {
			parkingSensorsService.updateParkingSensorAddressById(address, id);
		} catch (NullPointerException e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkiAreangResource - END updateParkingAreaAddressById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/parking-area/update/state/{state}/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateParkingAreaStateById(@PathVariable boolean state, @PathVariable Long id) {
		logger.info("ParkiAreangResource - START updateParkingAreaStateById");
		try {
			parkingSensorsService.updateParkingAreaValueById(state, id);
		} catch (NullPointerException e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkiAreangResource - END updateParkingAreaStateById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("sensor/parking-area/delete/{sensorId}")
	public ResponseEntity<Object> deleteParkingAreaBySensorId(@PathVariable Long sensorId) {
		logger.info("ParkingSensorResource - START deleteParkingSensorBySensorId");
		try {
			parkingSensorsService.deleteParkingSensorBySensorId(sensorId);
		} catch (NullPointerException e) {
			logger.error("ParkingSensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingSensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkiSensorngResource - END deleteParkingSensorBySensorId");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/parking-area/delete/{id}")
	public ResponseEntity<Object> deleteParkingAreaById(@PathVariable Long id) {
		logger.info("ParkingSensorResource - START deleteParkingSensorById");
		try {
			parkingSensorsService.deleteParkingSensorById(id);
		} catch (NullPointerException e) {
			logger.error("ParkingSensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			logger.error("ParkingSensorResources -  error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingSensorResource - END deleteAllParkingSensors");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/parking-area/delete/all")
	public ResponseEntity<Object> deleteAllParkingArea() {
		logger.info("ParkingSensorResource - START deleteAllParkingSensors");
		try {
			parkingSensorsService.deleteAllParkingSensors();
		} catch (Exception e) {
			logger.error("ParkingSensorResource -  error - deleteAllParkingSensors", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("ParkingSensorResource - END deleteAllParkingSensors");
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}