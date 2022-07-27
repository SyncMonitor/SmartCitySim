package it.synclab.smartparking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.synclab.smartparking.datasource.config.MySqlClient;
import it.synclab.smartparking.datasource.config.PostgreClient;
import it.synclab.smartparking.model.Marker;
import it.synclab.smartparking.model.MarkerList;
import it.synclab.smartparking.repository.ParkingAreaRepository;
import it.synclab.smartparking.repository.SensorsRepository;
import it.synclab.smartparking.repository.model.ParkingArea;
import it.synclab.smartparking.repository.model.Sensor;

@Component
public class ParkingService {

	@Value("${sensor.parking.url}")
	private String sensorDataUrl;

	@Autowired
	PostgreClient databaseClient;
	
//	@Autowired
//	DataSource databaseClient;

	@Autowired
	private SensorsRepository sensorsRepository;

	@Autowired
	private ParkingAreaRepository parkingAreaRepository;

	private static final Logger logger = LogManager.getLogger(ParkingService.class);

	public MarkerList convertXMLtoJson(String xml) {

		MarkerList markersList = null;

		try {
			logger.debug("ParkingService START convertXMLtoJson");
			JSONObject jsonObj = XML.toJSONObject(xml);
			String object = jsonObj.toString();
			ObjectMapper objectMapper = new ObjectMapper();
			markersList = objectMapper.readValue(object, MarkerList.class);

		} catch (Exception e) {
			logger.error("ParkingService - Error", e);
		}

		logger.debug("ParkingService END convertXMLtoJson");
		return markersList;
	}

	// Sensor's Services

	public MarkerList readSensorData() throws Exception {

		logger.debug("ParkingService START readSensorData");
		MarkerList markersList = null;

		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(sensorDataUrl).build();
			Response response = client.newCall(request).execute();
			String xmlSensorData = response.body().string();
			markersList = convertXMLtoJson(xmlSensorData);
		} catch (Exception e) {
			logger.error("ParkingService - Error", e);
		}

		logger.debug("ParkingService END readSensorData");
		return markersList;
	}

	public void saveSensorData(Sensor sensor) {

		logger.debug("ParkingService START saveSensorData");

		try {
			sensorsRepository.save(sensor);
		} catch (Exception e) {
			logger.error("ParkingService - Error", e);
		}

		logger.debug("ParkingService END saveSensorData");
	}

	public Sensor buildSensorFromMarker(Marker marker) {

		logger.debug("ParkingService START buildSensorFromMarker");
		Sensor s = new Sensor();

		List<ParkingArea> parkArea = new ArrayList<>();

		if (marker.getId() != null) {
			s.setId(marker.getId());
		}

		if (marker.getName() != null) {
			s.setName(marker.getName());
		}

		if (marker.getBattery() != null) {
			s.setBattery(marker.getBattery());
		}

//		Type attribute missing in XML file
//		if(m.getType() != null) {}
		s.setType("ParkingArea");

		s.setActive(marker.isActive());

		ParkingArea p = buildParkingAreaFromMarker(marker);
		parkArea.add(p);

		s.setParkingArea(parkArea);

		logger.debug("ParkingService END buildSensorFromMarker");

		return s;
	}

	public void writeSensorsData() {

		logger.debug("ParkingService START writeSensorData");

		try {
			MarkerList sensors = readSensorData();

			for (Marker m : sensors.getMarkers().getMarkers()) {
				Sensor s = buildSensorFromMarker(m);
				saveSensorData(s);
			}

		} catch (Exception e) {
			logger.error("parkingService - Error", e);
		}

		logger.debug("ParkingService END writeSensorData");

	}

	// Do this every 2 minutes
	@Scheduled(cron = "${polling.timer}")
	public void updateSensorsData() {
		
		

		logger.debug("ParkingService START updateSensorsData");

		boolean sensorUpdate = false;
		boolean parkAreaUpdate = false;

		try {
			MarkerList sensors = readSensorData();
			
			int markerListSize = sensors.getMarkers().markers.size();
			int sensorListFromDB = sensorsRepository.getAllSensors().size();
			
			logger.debug("Number of sensors from file:{}", markerListSize);
		
			logger.debug("Number of sensors from Data Base:{}", sensorListFromDB);
			
			if(markerListSize != sensorListFromDB) {
				logger.debug("Sensor DB is empty or there is a new sensor.");
				writeSensorsData();
			}


			for (Marker m : sensors.getMarkers().getMarkers()) {
				Sensor s = buildSensorFromMarker(m);
				ParkingArea p = buildParkingAreaFromMarker(m);

				Sensor aux = sensorsRepository.getSensorById(s.getId());
				ParkingArea tmp = parkingAreaRepository.getParkingAreaByFkSensorId(p.getSensorId());

				if (!aux.getName().equals(s.getName())) {
					updateSensorNameById(s.getName(), s.getId());
					if (!sensorUpdate) {
						sensorUpdate = true;
					}
				}

				if (!aux.getBattery().equals(s.getBattery())) {
					updateSensorBatteryById(s.getBattery(), s.getId());
					if (!sensorUpdate) {
						sensorUpdate = true;
					}
				}

				if (!aux.getType().equals(s.getType())) {
					updateSensorTypeById(s.getType(), s.getId());
					if (!sensorUpdate) {
						sensorUpdate = true;
					}
				}

				if (aux.isActive() != s.isActive()) {
					updateSensorStateById(s.isActive(), s.getId());
					if (!sensorUpdate) {
						sensorUpdate = true;
					}
				}

				if (!tmp.getLatitude().equals(p.getLatitude())) {
					// tmp.getId() because when extract data from sensor you don't
//					have p.Id because this is automatically assigned by DB
					updateParkingAreaLatitudeById(p.getLatitude(), tmp.getId());
					if (!parkAreaUpdate) {
						parkAreaUpdate = true;
					}
				}

				if (!tmp.getLongitude().equals(p.getLongitude())) {
					updateParkingAreaLongitudeById(p.getLongitude(), tmp.getId());
					if (!parkAreaUpdate) {
						parkAreaUpdate = true;
					}
				}

				if (!tmp.getAddress().equals(p.getAddress())) {
					updateParkingAreaAddressById(p.getAddress(), tmp.getId());
					if (!parkAreaUpdate) {
						parkAreaUpdate = true;
					}
				}

				if (tmp.getValue() != p.getValue()) {
					updateParkingAreaStateById(p.getValue(), tmp.getId());
					if (!parkAreaUpdate) {
						parkAreaUpdate = true;
					}
				}
			}

			if (sensorUpdate) {
				System.out.println("Sensors data updated");
			} else {
				System.out.println("No Sensors data to update");
			}

			if (parkAreaUpdate) {
				System.out.println("ParkingArea data updated");
			} else {
				System.out.println("No ParkingArea data to update");
			}

		} catch (Exception e) {
			logger.error("ParkingService ERROR updateSensorsData", e);
		}
		logger.debug("ParkingService END updateSensorsData");
	}

//	state = 0 : notWorking, 1 : working
	public boolean getSensorState(Long sensorId) {
		logger.debug("ParkingService START getSensorState - sensorId{}", sensorId);
		boolean state = sensorsRepository.getSensorState(sensorId);
		System.out.println(state);
		logger.debug("ParkingService END getSensorState - sensorState:{} - sensorId:{}", state, sensorId);

		return state;
	}

	public List<Sensor> getSensorsByName(String name) {
		logger.debug("ParkingService START getSensorsByName - sensorName:{}", name);
		List<Sensor> sensors = sensorsRepository.getSensorsByName(name);
		logger.debug("ParkingService END getSensorByName - snesorName:{} - sensorsListSize:{}", sensors.size());
		return sensors;
	}

	public Sensor getSensorById(Long sensorId) {
		logger.debug("ParkingService START getSensorById - sensorId:{}", sensorId);
		Sensor s = sensorsRepository.getSensorById(sensorId);
		logger.debug("ParkingService END getSensorById - sensorName:{} - sensorId:{}", s.getName(), s.getId());
		return s;
	}

	public List<Sensor> getSensorsByType(String type) {
		logger.debug("ParkingService START getSensorsByType -sensorType");
		List<Sensor> sensors = sensorsRepository.getSensorsByType(type);
		logger.debug("ParkingService END getSensorsByType - sensorsType:{} - sensorsListSize:{}", type, sensors.size());
		return sensors;
	}

	public List<Sensor> getSensorByNameStartingWith(String str) {
		logger.debug("ParkingService START getSensorByNameStartingWith - startingSequence:{}", str);
		List<Sensor> sensors = sensorsRepository.getSensorByNameStartingWith(str);
		logger.debug("ParkingService END getSensorByNameStartingWith - startingSequence:{} - sensorsListSize:{}", str,
				sensors.size());
		return sensors;
	}

	public List<Sensor> getSensorByNameEndingWith(String str) {
		logger.debug("ParkingService START getSensorByNameEndingWith - endingSequence:{}", str);
		List<Sensor> sensors = sensorsRepository.getSensorByNameEndingWith(str);
		logger.debug("ParkingService END getSensorByNameEndingWith -  - startingSequence:{} - sensorsListSize:{}", str,
				sensors.size());
		return sensors;
	}

	public List<Sensor> getSensorByNameContaining(String str) {
		logger.debug("ParkingService START getSensorByNameContaining - Sequence:{}", str);
		List<Sensor> sensors = sensorsRepository.getSensorByNameContaining(str);
		logger.debug("ParkingService END getSensorByNameContaining - Sequence:{} - sensorsListSize:{}", str,
				sensors.size());
		return sensors;
	}

	public List<Sensor> getSensorByIsActiveTrue() {
		logger.debug("ParkingService START getSensorByIsActiveTrue");
		List<Sensor> sensors = sensorsRepository.getSensorByIsActiveTrue();
		logger.debug("ParkingService END getSensorByIsActiveTrue - sensorsListSize:{}", sensors.size());
		return sensors;
	}

	public List<Sensor> getSensorByIsActiveFalse() {
		logger.debug("ParkingService START getSensorByIsActiveFalse");
		List<Sensor> sensors = sensorsRepository.getSensorByIsActiveFalse();
		logger.debug("ParkingService END getSensorByIsActiveFalse - sensorsListSize:{}", sensors.size());
		return sensors;
	}

	public void updateSensorNameById(String name, Long sensorId) {
		logger.debug("ParkingService START updateSensorNameById - sensorName:{} - sensorId:{}", name, sensorId);
		sensorsRepository.updateSensorName(name, sensorId);
		logger.debug("ParkingService END updateSensorNameById - sensorName:{} - sensorId:{}", name, sensorId);

	}

	public void updateSensorBatteryById(String battery, Long sensorId) {
		logger.debug("ParkingService START updateSensorBatteryById - sensorBattery:{} - sensorId:{}", battery,
				sensorId);
		sensorsRepository.updateSensorBattery(battery, sensorId);
		logger.debug("ParkingService END updateSensorBatteryById - sensorBattery:{} - sensorId:{}", battery, sensorId);
	}

	public void updateSensorTypeById(String type, Long sensorId) {
		logger.debug("ParkingService START updateSensorTypeById - sensorType:{} - sensorId:{}", type, sensorId);
		sensorsRepository.updateSensorTypeById(type, sensorId);
		logger.debug("ParkingService END updateSensorTypeById - sensorType:{} - sensorId:{}", type, sensorId);
	}

	public void updateSensorStateById(boolean state, Long id) {
		logger.debug("ParkingService START updateSensorStateById - sensorState:{} - sensorId:{}", state, id);
		sensorsRepository.updateStateById(state, id);
		logger.debug("ParkingService END updateSensorStateById - sensorState:{} - sensorId:{}", state, id);
	}

	public void deleteSensorById(Long sensorId) {
		logger.debug("ParkingService START deleteSensorById - sensorId:{}", sensorId);
		sensorsRepository.deleteById(sensorId);
		logger.debug("ParkingService END deleteSensorById - sensorId:{}", sensorId);

	}

	public void deleteAlSensors() {
		logger.debug("ParkingService START deleteAlSensors ");
		sensorsRepository.deleteAll();
		logger.debug("ParkingService END deleteAlSensors ");
	}

	// ParkingArea's Services

	public MarkerList readParkingAreaData() throws Exception {
		logger.debug("ParkingService START readParkingAreaData");
		MarkerList markersList = null;

		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(sensorDataUrl).build();
			Response response = client.newCall(request).execute();
			String xmlSensorData = response.body().string();
			markersList = convertXMLtoJson(xmlSensorData);
		} catch (Exception e) {
			logger.error("ParkingService - Error", e);
		}

		logger.debug("ParkingService END readParkingAreaData");
		return markersList;
	}

//	public void saveParkingAreaData(ParkingArea parkArea) {
//		try {
//			parkingAreaRepository.save(parkArea);
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//	}

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

		parkArea.setValue(marker.isActive());

		logger.debug("ParkingService END buildParkingAreaFromMarker");

		return parkArea;
	}

	public ParkingArea getParkingAreaById(Long id) {
		logger.debug("ParkingService START getParkingAreaById - parkingAreaId:{}", id);
		ParkingArea p = parkingAreaRepository.getParkingAreaById(id);
		logger.debug("ParkingService END getParkingAreaById - parkingAreaId:{}", p.getId());
		return p;
	}

	public ParkingArea getParkingAreaBySensorId(Long sensorId) {
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
		List<ParkingArea> parkArea = parkingAreaRepository.getParkingAreaByValueTrue();
		logger.debug("ParkingService END getFreeParkingArea");
		return parkArea;
	}

	public List<ParkingArea> getOccupyParkingArea() {
		logger.debug("ParkingService START getOccupyParkingArea");
		List<ParkingArea> parkArea = parkingAreaRepository.getParkingAreaByValueFalse();
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

	public void updateParkingAreaLatitudeById(String latitude, Long id) {
		logger.debug("ParkingService START updateParkingAreaLatitudeById - parkAreaId{} - latitude:{}", id, latitude);
		parkingAreaRepository.updateLatitudeById(latitude, id);
		logger.debug("ParkingService END updateParkingAreaLatitudeById - parkAreaId{} - latitude:{}", id, latitude);
	}

	public void updateParkingAreaLongitudeById(String longitude, Long id) {
		logger.debug("ParkingService START updateParkingAreaLongitudeById - parkAreaId{} - longitude:{}", id, longitude);
		parkingAreaRepository.updateLongitudeById(longitude, id);
		logger.debug("ParkingService END updateParkingAreaLongitudeById - parkAreaId{} - longitude:{}", id, longitude);
	}

	public void updateParkingAreaAddressById(String address, Long id) {
		logger.debug("ParkingService START updateParkingAreaAddressById - parkAreaId{} - address:{}", id, address);
		parkingAreaRepository.updateAddressById(address, id);
		logger.debug("ParkingService END updateParkingAreaAddressById - parkAreaId{} - address:{}", id, address);
	}

//	state = 0 : free, 1 : occupy
	public void updateParkingAreaStateById(boolean state, Long id) {
		logger.debug("ParkingService START updateParkingAreaStateById - parkAreaId{} - state:{}", id, state);
		parkingAreaRepository.setStateById(state, id);
		logger.debug("ParkingService END updateParkingAreaStateById - parkAreaId{} - state:{}", id, state);
	}
}
