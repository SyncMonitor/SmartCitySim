package it.synclab.smartparking.resources;

import java.time.LocalDateTime;
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

import it.synclab.smartparking.repository.model.ParkingArea;
import it.synclab.smartparking.service.ParkingAreaServices;



@RestController
@RequestMapping("/scs")
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ParkingAreaResources {

	private static final Logger logger = LogManager.getLogger(ParkingAreaResources.class);
	
	@Autowired
	private ParkingAreaServices parkingAreaService;

	@GetMapping("/parking-area/all")
	@ResponseBody
	public ResponseEntity<Object> getAllParkingArea() {
		logger.info("ParkingAreaResources - START getAllParkingArea");
		List<ParkingArea> parkingAreas;
		// Security user check
		try {
			parkingAreas = parkingAreaService.getAllParkingAreas();
		} catch (Exception e) {
			logger.error("ParkingAreaResources -  error - getAllParkingArea", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingAreaResources - END getAllParkingArea");
		return ResponseEntity.status(HttpStatus.OK).body(parkingAreas);
	}

	@GetMapping("/parking-area/data/{id}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaData(@PathVariable Long id) {
		logger.info("ParkingAreaResources - START getParkingAreaData");
		ParkingArea p;
		// Security user check
		try {
			p = parkingAreaService.getParkingAreaById(id);
		} catch (Exception e) {
			logger.error("ParkingAreaResources -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingAreaResources - END getParkingAreaData");
		return ResponseEntity.status(HttpStatus.OK).body(p);
	}

	@GetMapping("/parking-area/data-from-sensor/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaDataBySensorId(@PathVariable Long sensorId) {
		logger.info("ParkingAreaResource - START getParkingAreaDataBySensorId");
		ParkingArea p;
		// Security user check
		try {
			p = parkingAreaService.getParkingAreaBySensorId(sensorId);
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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
			latitude = parkingAreaService.getParkingAreaLatitudeById(id);
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingAreaResource - END getParkingAreaLatitudeById");
		return ResponseEntity.status(HttpStatus.OK).body(latitude);
	}

	@GetMapping("/parking-area/longitude/{id}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaLongitudeById(@PathVariable Long id) {
		logger.info("ParkingAreaResource - START getParkingAreaLongitudeById");
		String latitude;
		// Security user check
		try {
			latitude = parkingAreaService.getParkingAreaLongitudeById(id);
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkingAreaResource - END getParkingAreaLongitudeById");
		return ResponseEntity.status(HttpStatus.OK).body(latitude);
	}

	@GetMapping("/parking-area/address/{id}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaAddressById(@PathVariable Long id) {
		logger.info("ParkingAreaResource - START getParkingAreaLongitudeById");
		String address;
		// Security user check
		try {
			address = parkingAreaService.getParkingAreaAddressById(id);
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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
			state = parkingAreaService.getParkingAreaStateById(id);
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkiAreangResource - END getParkingAreaStateById");
		return ResponseEntity.status(HttpStatus.OK).body(state);
	}

	@GetMapping("/parking-area/free")
	@ResponseBody
	public ResponseEntity<Object> getFreeParkingArea() {
		logger.info("ParkiAreangResource - START getFreeParkingArea");
		List<ParkingArea> state;
		// Security user check
		try {
			state = parkingAreaService.getFreeParkingArea();
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkiAreangResource - END getFreeParkingArea");
		return ResponseEntity.status(HttpStatus.OK).body(state);
	}

	@GetMapping("/parking-area/occupy")
	@ResponseBody
	public ResponseEntity<Object> getOccupyParkingArea() {
		logger.info("ParkiAreangResource - START getOccupyParkingArea");
		List<ParkingArea> state;
		// Security user check
		try {
			state = parkingAreaService.getOccupyParkingArea();
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkiAreangResource - END getOccupyParkingArea");
		return ResponseEntity.status(HttpStatus.OK).body(state);
	}

	@GetMapping("/parking-area/{latitude}/{longitude}")
	@ResponseBody
	public ResponseEntity<Object> getParkingAreaByLatitudeAndLongitude(@PathVariable String latitude,
			@PathVariable String longitude) {
		logger.info("ParkiAreangResource - START getParkingAreaByLatitudeAndLongitude");
		ParkingArea parkArea;
		// Security user check
		try {
			parkArea = parkingAreaService.getParkingAreaByLatitudeAndLongitude(latitude, longitude);
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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
			date = parkingAreaService.getParkingAreaLastUpdateDateBySensorId(sensorId);
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  ERROR - getParkingAreaLastUpdateDateBySensorId", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkiAreangResource - END getParkingAreaLastUpdateDateBySensorId");
		return ResponseEntity.status(HttpStatus.OK).body(date);
	}

	@PutMapping("/parking-area/update/latitude/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateParkingAreaLatitudeById(@PathVariable Long id, @RequestBody String latitude) {
		logger.info("ParkiAreangResource - START updateParkingAreaLatitudeById");
		try {
			parkingAreaService.updateParkingAreaLatitudeById(latitude, id);
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkiAreangResource - END updateParkingAreaLatitudeById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/parking-area/update/longitude/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateParkingAreaLongitudeById(@PathVariable Long id, @RequestBody String longitude) {
		logger.info("ParkiAreangResource - START updateParkingAreaLatitudeById");
		try {
			parkingAreaService.updateParkingAreaLongitudeById(longitude, id);
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkiAreangResource - END updateParkingAreaLongitudeById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/parking-area/update/address/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateParkingAreaAddressById(@PathVariable Long id, @RequestBody String address) {
		logger.info("ParkiAreangResource - START updateParkingAreaAddressById");
		try {
			parkingAreaService.updateParkingAreaAddressById(address, id);
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkiAreangResource - END updateParkingAreaAddressById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/parking-area/update/state/{state}/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateParkingAreaStateById(@PathVariable boolean state, @PathVariable Long id) {
		logger.info("ParkiAreangResource - START updateParkingAreaStateById");
		try {
			parkingAreaService.updateParkingAreaValueById(state, id);
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  error", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkiAreangResource - END updateParkingAreaStateById");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("sensor/parking-area/delete/{sensorId}")
	public ResponseEntity<Object> deleteParkingAreaBySensorId(@PathVariable Long sensorId) {
		logger.info("ParkiAreangResource - START deleteParkingAreaBySensorId");
		try {
			parkingAreaService.deleteParkingAreaBySensorId(sensorId);
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  error - deleteParkingAreaBySensorId", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkiAreangResource - END deleteParkingAreaBySensorId");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/parking-area/delete/{id}")
	public ResponseEntity<Object> deleteParkingAreaById(@PathVariable Long id) {
		logger.info("ParkiAreangResource - START deleteParkingAreaById");
		try {
			parkingAreaService.deleteParkingAreaById(id);
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  error - deleteParkingAreaById", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkiAreangResource - END deleteAllParkingArea");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/parking-area/delete/all")
	public ResponseEntity<Object> deleteAllParkingArea() {
		logger.info("ParkiAreangResource - START deleteAllParkingArea");
		try {
			parkingAreaService.deleteAllParkingArea();
		} catch (Exception e) {
			logger.error("ParkiAreangResource -  error - deleteAllParkingArea", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		logger.info("ParkiAreangResource - END deleteAllParkingArea");
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}