package it.synclab.smartparking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.synclab.smartparking.model.Marker;
import it.synclab.smartparking.repository.ParkingSensorsRepository;
import it.synclab.smartparking.repository.SensorsRepository;
import it.synclab.smartparking.repository.model.ParkingSensors;

@Component
public class ParkingSensorsServices {
	
	@Value("${sensor.parking.url}")
	private String sensorDataUrl;
	
	@Autowired
	private ParkingSensorsRepository parkingAreaRepository;
	
	@Autowired
	private SensorsRepository sensorsRepository;
	
	private static final Logger logger = LogManager.getLogger(ParkingSensorsServices.class);


	public ParkingSensors buildParkingSensorFromMarker(Marker marker) {
		logger.debug("ParkingService START buildParkingSensorFromMarker");
		ParkingSensors parkSensor = new ParkingSensors();
		if (marker.getId() != null) {
			parkSensor.getSensors().setId(marker.getId());;
		}
		if (marker.getLat() != null) {
			parkSensor.setLatitude(marker.getLat());
		}
		if (marker.getLng() != null) {
			parkSensor.setLongitude(marker.getLng());
		}
		if (marker.getAddress() != null) {
			parkSensor.setAddress(marker.getAddress());
		}
		parkSensor.setValue(marker.getState());
		parkSensor.setTimestamp(LocalDateTime.now());
		logger.debug("ParkingService END buildParkingSensorFromMarker");
		return parkSensor;
	}

	public List<ParkingSensors> getAllParkingSensors() {
		logger.debug("ParkingService START getAllParkingAreas");
		List<ParkingSensors> parkingAreas = parkingAreaRepository.getAllParkingSensors();
		logger.debug("ParkingService END getAllParkingAreas");
		return parkingAreas;
	}

	public ParkingSensors getParkingSensorById(Long id) {
		logger.debug("ParkingService START getParkingAreaById - parkingAreaId:{}", id);
		ParkingSensors p = parkingAreaRepository.getParkingSensorsById(id);
		logger.debug("ParkingService END getParkingAreaById - parkingAreaId:{}", p.getId());
		return p;
	}

	public ParkingSensors getParkingSensorBySensorId(Long sensorId) throws NullPointerException {
		logger.debug("ParkingService START getParkingAreaBySensorId - sensorId:{}", sensorId);
		ParkingSensors p = parkingAreaRepository.getParkingSensorsBySensorId(sensorId);
		logger.debug("ParkingService END getParkingAreaBySensorId - sensorId:{}", p.getId());
		return p;
	}

	public String getParkingSensorLatitudeById(Long id) {
		logger.debug("ParkingService START getParkingAreaLatitudeById - parkingAreaId:{}", id);
		String latitude = parkingAreaRepository.getLatitudeById(id);
		logger.debug("ParkingService END getParkingAreaLatitudeById - parkingAreaId:{} - latitude:{}", id, latitude);
		return latitude;
	}

	public String getParkingSensorLongitudeById(Long id) {
		logger.debug("ParkingService START getParkingAreaLongitudeById - parkingAreaId:{}", id);
		String longitude = parkingAreaRepository.getLongitudeById(id);
		logger.debug("ParkingService END getParkingAreaLongitudeById - parkingAreaId:{} - latitude:{}", id, longitude);
		return longitude;
	}

	public String getParkingSensorAddressById(Long id) {
		logger.debug("ParkingService START getParkingAreaAddressById - parkingAreaId:{}", id);
		String address = parkingAreaRepository.getAddressById(id);
		logger.debug("ParkingService END getParkingAreaAddressById - parkingAreaId:{} - address:{}", id, address);
		return address;

	}

	public boolean getParkingSensorStateById(Long id) {
		logger.debug("ParkingService START getParkingAreaStateById - parkingAreaId:{}", id);
		boolean state = parkingAreaRepository.getStateById(id);
		logger.debug("ParkingService END getParkingAreaStateById - parkingAreaId:{} - state:{}", id, state);
		return state;

	}

	public List<ParkingSensors> getFreeParkingSensors() {
		logger.debug("ParkingService START getFreeParkingArea");
		List<ParkingSensors> parkArea = parkingAreaRepository.getParkingSensorsByValueFalse();
		logger.debug("ParkingService END getFreeParkingArea");
		return parkArea;
	}

	public List<ParkingSensors> getOccupiedParkingSensors() {
		logger.debug("ParkingService START getOccupiedParkingArea");
		List<ParkingSensors> parkArea = parkingAreaRepository.getParkingSensorsByValueTrue();
		logger.debug("ParkingService START getOccupiedParkingArea");
		return parkArea;
	}

	public ParkingSensors getParkingSensorByLatitudeAndLongitude(String latitude, String longitude) {
		logger.debug("ParkingService START getParkingAreaByLatitudeAndLongitude - latitude:{} - longitude{}", latitude,
				longitude);
		ParkingSensors p = parkingAreaRepository.getParkingSensorsByLatitudeAndLongitude(latitude, longitude);
		logger.debug(
				"ParkingService END getParkingAreaByLatitudeAndLongitude - parkingAreaId:{} - latitude:{} - longitude{}",
				p.getId(), latitude, longitude);
		return p;
	}

	public LocalDateTime getParkingSensorLastUpdateDateBySensorId(Long sensorId) {
		logger.debug("ParkingService START getParkingAreaLastUpdateDateBySensorId - sensorId:{}", sensorId);
		LocalDateTime date = parkingAreaRepository.getTimestampBySensorId(sensorId);
		logger.debug("ParkingService END getParkingAreaLastUpdateDateBySensorId - sensorId:{} - date:{}", sensorId,
				date);
		return date;
	}

	public void updateParkingSensorLatitudeById(String latitude, Long id) {
		logger.debug("ParkingService START updateParkingAreaLatitudeById - parkAreaId{} - latitude:{}", id, latitude);
		parkingAreaRepository.updateLatitudeById(latitude, id);
		logger.debug("ParkingService END updateParkingAreaLatitudeById - parkAreaId{} - latitude:{}", id, latitude);
	}

	public void updateParkingSensorLongitudeById(String longitude, Long id) {
		logger.debug("ParkingService START updateParkingAreaLongitudeById - parkAreaId{} - longitude:{}", id,
				longitude);
		parkingAreaRepository.updateLongitudeById(longitude, id);
		logger.debug("ParkingService END updateParkingAreaLongitudeById - parkAreaId{} - longitude:{}", id, longitude);
	}

	public void updateParkingSensorAddressById(String address, Long id) {
		logger.debug("ParkingService START updateParkingAreaAddressById - parkAreaId{} - address:{}", id, address);
		parkingAreaRepository.updateAddressById(address, id);
		logger.debug("ParkingService END updateParkingAreaAddressById - parkAreaId{} - address:{}", id, address);
	}
	
	public void updateParkingSensorLastUpdateBySensorId(LocalDateTime date, Long sensorId) {
		logger.debug("ParkingService START updateParkingAreaAddressById - parkAreaId{} - address:{}", sensorId, date);
		parkingAreaRepository.updateTimestampBySensorsId(date, sensorId);
		logger.debug("ParkingService END updateParkingAreaAddressById - parkAreaId{} - address:{}", sensorId, date);
	}
	
//	state = 0 : free, 1 : occupy
	public void updateParkingAreaValueById(boolean state, Long id) {
		logger.debug("ParkingService START updateParkingAreaValueById - parkAreaId{} - state:{}", id, state);
		parkingAreaRepository.updateValueById(state, id);
		logger.debug("ParkingService END updateParkingAreaValueById - parkAreaId{} - state:{}", id, state);
	}

	public void deleteParkingSensorBySensorId(Long sensorId) {
		logger.debug("ParkingService START deleteParkingSensorBySensorId");
		parkingAreaRepository.deleteParkingSensorsBySensorsId(sensorId);
		logger.debug("ParkingService END deleteParkingSensorBySensorId");
	}

	public void deleteParkingSensorById(Long id) {
		logger.debug("ParkingService START deleteParkingSensorById");
		parkingAreaRepository.deleteParkingSensorsById(id);
		logger.debug("ParkingService END deleteParkingSensorById");
	}

	public void deleteAllParkingSensors() {
		logger.debug("ParkingService START deleteAllSensors ");
		parkingAreaRepository.deleteAll();
		logger.debug("ParkingService END deleteAllSensors ");
	}
}