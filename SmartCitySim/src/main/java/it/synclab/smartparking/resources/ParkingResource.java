package it.synclab.smartparking.resources;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.synclab.smartparking.service.ParkingService;
import it.synclab.smartparking.model.MarkerList;
import it.synclab.smartparking.model.Markers;

@RestController
@RequestMapping("/parking")
public class ParkingResource {

	private Logger logger = LogManager.getLogger(ParkingResource.class);

	@Autowired
	private ParkingService parkingService;

	

	@GetMapping("/sensor/{sensorId}")
	@ResponseBody
	public ResponseEntity<Object> getSensorState(@PathVariable String sensorId) {
		logger.info("ParkingResource - START getSensorState");
		int sensorState = 0;
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
	
	@GetMapping("/sensor/getAllData")
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

}
