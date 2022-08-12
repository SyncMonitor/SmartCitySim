package it.synclab.smartparking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.synclab.smartparking.model.Marker;
import it.synclab.smartparking.repository.ParkingAreaRepository;
import it.synclab.smartparking.repository.model.ParkingArea;

@Component
public class ParkingAreaServices {
	
	@Value("${sensor.parking.url}")
	private String sensorDataUrl;
	
	@Autowired
	private ParkingAreaRepository parkingAreaRepository;
	
	private static final Logger logger = LogManager.getLogger(ParkingAreaServices.class);


	public ParkingArea buildParkingAreaFromMarker(Marker marker) {
		logger.debug("ParkingService START buildParkingAreaFromMarker");
		ParkingArea parkArea = new ParkingArea();
		if (marker.getId() != null) {
			parkArea.setSensorId(marker.getId());
		}
		if (marker.getLat() != null) {
			parkArea.setLatitude(marker.getLat());
		}
		if (marker.getLng() != null) {
			parkArea.setLongitude(marker.getLng());
		}
		if (marker.getAddress() != null) {
			parkArea.setAddress(marker.getAddress());
		}
		parkArea.setValue(marker.getState());
		parkArea.setLastUpdate(LocalDateTime.now());
		logger.debug("ParkingService END buildParkingAreaFromMarker");
		return parkArea;
	}

	public List<ParkingArea> getAllParkingAreas() {
		logger.debug("ParkingService START getAllParkingAreas");
		List<ParkingArea> parkingAreas = parkingAreaRepository.getAllParkingArea();
		logger.debug("ParkingService END getAllParkingAreas");
		return parkingAreas;
	}

	public ParkingArea getParkingAreaById(Long id) {
		logger.debug("ParkingService START getParkingAreaById - parkingAreaId:{}", id);
		ParkingArea p = parkingAreaRepository.getParkingAreaById(id);
		logger.debug("ParkingService END getParkingAreaById - parkingAreaId:{}", p.getId());
		return p;
	}

	public ParkingArea getParkingAreaBySensorId(Long sensorId) throws NullPointerException {
		logger.debug("ParkingService START getParkingAreaBySensorId - sensorId:{}", sensorId);
		ParkingArea p = parkingAreaRepository.getParkingAreaByFkSensorId(sensorId);
		logger.debug("ParkingService END getParkingAreaBySensorId - sensorId:{}", p.getId());
		return p;
	}

	public String getParkingAreaLatitudeById(Long id) {
		logger.debug("ParkingService START getParkingAreaLatitudeById - parkingAreaId:{}", id);
		String latitude = parkingAreaRepository.getLatitudeById(id);
		logger.debug("ParkingService END getParkingAreaLatitudeById - parkingAreaId:{} - latitude:{}", id, latitude);
		return latitude;
	}

	public String getParkingAreaLongitudeById(Long id) {
		logger.debug("ParkingService START getParkingAreaLongitudeById - parkingAreaId:{}", id);
		String longitude = parkingAreaRepository.getLongitudeById(id);
		logger.debug("ParkingService END getParkingAreaLongitudeById - parkingAreaId:{} - latitude:{}", id, longitude);
		return longitude;
	}

	public String getParkingAreaAddressById(Long id) {
		logger.debug("ParkingService START getParkingAreaAddressById - parkingAreaId:{}", id);
		String address = parkingAreaRepository.getAddressById(id);
		logger.debug("ParkingService END getParkingAreaAddressById - parkingAreaId:{} - address:{}", id, address);
		return address;

	}

	public boolean getParkingAreaStateById(Long id) {
		logger.debug("ParkingService START getParkingAreaStateById - parkingAreaId:{}", id);
		boolean state = parkingAreaRepository.getStateById(id);
		logger.debug("ParkingService END getParkingAreaStateById - parkingAreaId:{} - state:{}", id, state);
		return state;

	}

	public List<ParkingArea> getFreeParkingArea() {
		logger.debug("ParkingService START getFreeParkingArea");
		List<ParkingArea> parkArea = parkingAreaRepository.getParkingAreaByValueFalse();
		logger.debug("ParkingService END getFreeParkingArea");
		return parkArea;
	}

	public List<ParkingArea> getOccupyParkingArea() {
		logger.debug("ParkingService START getOccupyParkingArea");
		List<ParkingArea> parkArea = parkingAreaRepository.getParkingAreaByValueTrue();
		logger.debug("ParkingService START getOccupyParkingArea");
		return parkArea;
	}

	public ParkingArea getParkingAreaByLatitudeAndLongitude(String latitude, String longitude) {
		logger.debug("ParkingService START getParkingAreaByLatitudeAndLongitude - latitude:{} - longitude{}", latitude,
				longitude);
		ParkingArea p = parkingAreaRepository.getParkingAreaByLatitudeAndLongitude(latitude, longitude);
		logger.debug(
				"ParkingService END getParkingAreaByLatitudeAndLongitude - parkingAreaId:{} - latitude:{} - longitude{}",
				p.getId(), latitude, longitude);
		return p;
	}

	public LocalDateTime getParkingAreaLastUpdateDateBySensorId(Long sensorId) {
		logger.debug("ParkingService START getParkingAreaLastUpdateDateBySensorId - sensorId:{}", sensorId);
		LocalDateTime date = parkingAreaRepository.getLastUpdateBySensoId(sensorId);
		logger.debug("ParkingService END getParkingAreaLastUpdateDateBySensorId - sensorId:{} - date:{}", sensorId,
				date);
		return date;
	}

	public void updateParkingAreaLatitudeById(String latitude, Long id) {
		logger.debug("ParkingService START updateParkingAreaLatitudeById - parkAreaId{} - latitude:{}", id, latitude);
		parkingAreaRepository.updateLatitudeById(latitude, id);
		logger.debug("ParkingService END updateParkingAreaLatitudeById - parkAreaId{} - latitude:{}", id, latitude);
	}

	public void updateParkingAreaLongitudeById(String longitude, Long id) {
		logger.debug("ParkingService START updateParkingAreaLongitudeById - parkAreaId{} - longitude:{}", id,
				longitude);
		parkingAreaRepository.updateLongitudeById(longitude, id);
		logger.debug("ParkingService END updateParkingAreaLongitudeById - parkAreaId{} - longitude:{}", id, longitude);
	}

	public void updateParkingAreaAddressById(String address, Long id) {
		logger.debug("ParkingService START updateParkingAreaAddressById - parkAreaId{} - address:{}", id, address);
		parkingAreaRepository.updateAddressById(address, id);
		logger.debug("ParkingService END updateParkingAreaAddressById - parkAreaId{} - address:{}", id, address);
	}
	
	public void updateParkingAreaLastUpdateBySensorId(LocalDateTime date, Long sensorId) {
		logger.debug("ParkingService START updateParkingAreaAddressById - parkAreaId{} - address:{}", sensorId, date);
		parkingAreaRepository.updateLastUpdateBySensorId(date, sensorId);
		logger.debug("ParkingService END updateParkingAreaAddressById - parkAreaId{} - address:{}", sensorId, date);
	}

//	state = 0 : free, 1 : occupy
	public void updateParkingAreaValueById(boolean state, Long id) {
		logger.debug("ParkingService START updateParkingAreaValueById - parkAreaId{} - state:{}", id, state);
		parkingAreaRepository.updateValueById(state, id);
		logger.debug("ParkingService END updateParkingAreaValueById - parkAreaId{} - state:{}", id, state);
	}

	public void deleteParkingAreaBySensorId(Long sensorId) {
		logger.debug("ParkingService START deleteParkingAreaBySensorId");
		parkingAreaRepository.deleteParkingAreaBySensorId(sensorId);
		logger.debug("ParkingService END deleteParkingAreaBySensorId");
	}

	public void deleteParkingAreaById(Long id) {
		logger.debug("ParkingService START deleteParkingAreaById");
		parkingAreaRepository.deleteParkingAreaById(id);
		logger.debug("ParkingService END deleteParkingAreaById");
	}

	public void deleteAllParkingArea() {
		logger.debug("ParkingService START deleteAlSensors ");
		parkingAreaRepository.deleteAll();
		logger.debug("ParkingService END deleteAlSensors ");
	}
}