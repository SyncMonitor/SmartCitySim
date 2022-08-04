package it.synclab.smartparking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import it.synclab.smartparking.datasource.config.MySqlClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.synclab.smartparking.datasource.config.PostgreClient;
import it.synclab.smartparking.model.Maintainer;
import it.synclab.smartparking.model.Marker;
import it.synclab.smartparking.model.MarkerList;
import it.synclab.smartparking.repository.ParkingAreaRepository;
import it.synclab.smartparking.repository.SensorsMaintainerRepository;
import it.synclab.smartparking.repository.SensorsRepository;
import it.synclab.smartparking.repository.model.ParkingArea;
import it.synclab.smartparking.repository.model.Sensor;
import it.synclab.smartparking.repository.model.SensorsMaintainer;

@Component
public class ParkingService {

	@Value("${sensor.parking.url}")
	private String sensorDataUrl;

	@Value("${mail.reciver.new}")
	private String mailReciver;

	@Value("${mail.message.low.battery.start}")

	private String lowBatteryStartMessage;
	@Value("${mail.message.sensor.off.start}")
	private String sensorOffStartMessage;
	
	@Value("${mail.message.low.battery.end}")
	private String lowBatteryEndMessage;

	@Value("${mail.message.sensor.off.end}")
	private String sensorOffEndMessage;

	@Value("${mail.subject.battery.low}")
	private String lowBatterySubject;

	@Value("${mail.subject.sensor.off}")
	private String sensorOffSubject;

	@Autowired
	PostgreClient databaseClient;

//	@Autowired
//	DataSource databaseClient;

	private boolean sentMailLB = false;
	private boolean sentMailSO = false;

	@Autowired
	private SensorsRepository sensorsRepository;

	@Autowired
	private ParkingAreaRepository parkingAreaRepository;

	@Autowired
	private SensorsMaintainerRepository sensorsMaintainerRepository;

	@Autowired
	private MailService mailService;

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

		try {
			MarkerList sensors = readSensorData();
			
			boolean sensorUpdate = false;
			boolean parkAreaUpdate = false;

//			Check Sensors Status, eventually add new sensors
			
			int markerListSize = sensors.getMarkers().markers.size();
			int sensorListFromDB = sensorsRepository.getAllSensors().size();

			logger.debug("Number of sensors from file:{}", markerListSize);

			logger.debug("Number of sensors from Data Base:{}", sensorListFromDB);
			
			if (markerListSize != sensorListFromDB) {
				logger.debug("Sensor DB is empty or there is a new sensor.");
				writeSensorsData();
				//TODO update this method.
				writeSensorsMaintainerData();
			}
			//update DB data

			for (Marker m : sensors.getMarkers().getMarkers()) {
				Sensor sensor = buildSensorFromMarker(m);
				ParkingArea parkArea = buildParkingAreaFromMarker(m);
//				SensorsMaintainer maintainer = buildSensorsMaintainerFromMarker(m);

				Sensor sensorFromDB = sensorsRepository.getSensorById(sensor.getId());
				ParkingArea parkingAreaFromDB = parkingAreaRepository.getParkingAreaByFkSensorId(parkArea.getFkSensorId());

				if (!sensorFromDB.getName().equals(sensor.getName())) {
					updateSensorNameById(sensor.getName(), sensor.getId());
					if (!sensorUpdate) {
						sensorUpdate = true;
					}
				}

				if (!sensorFromDB.getBattery().equals(sensor.getBattery())) {
					updateSensorBatteryById(sensor.getBattery(), sensor.getId());
//					if(Float.parseFloat(sensor.getBattery()) < 3) {
//						updateSensorMaintainerToBeRepairedById(true, sensor.getId());
//					}
					if (!sensorUpdate) {
						sensorUpdate = true;
					}
				}

				if (!sensorFromDB.getType().equals(sensor.getType())) {
					updateSensorTypeById(sensor.getType(), sensor.getId());
					if (!sensorUpdate) {
						sensorUpdate = true;
					}
				}

				if (sensorFromDB.isActive() != sensor.isActive()) {
					updateSensorStateById(sensor.isActive(), sensor.getId());
//					updateSensorMaintainerToBeRepairedById(true, sensor.getId());
					if (!sensorUpdate) {
						sensorUpdate = true;
					}
				}

				if (!parkingAreaFromDB.getLatitude().equals(parkArea.getLatitude())) {
					// tmp.getId() because when extract data from sensor you don't
//					have p.Id because this is automatically assigned by DB
					updateParkingAreaLatitudeById(parkArea.getLatitude(), parkingAreaFromDB.getId());
					if (!parkAreaUpdate) {
						parkAreaUpdate = true;
					}
				}

				if (!parkingAreaFromDB.getLongitude().equals(parkArea.getLongitude())) {
					updateParkingAreaLongitudeById(parkArea.getLongitude(), parkingAreaFromDB.getId());
					if (!parkAreaUpdate) {
						parkAreaUpdate = true;
					}
				}

				if (!parkingAreaFromDB.getAddress().equals(parkArea.getAddress())) {
					updateParkingAreaAddressById(parkArea.getAddress(), parkingAreaFromDB.getId());
					if (!parkAreaUpdate) {
						parkAreaUpdate = true;
					}
				}
				
				//value = state
				if (parkingAreaFromDB.getValue() != parkArea.getValue()) {
					updateParkingAreaValueById(parkArea.getValue(), parkingAreaFromDB.getId());
					updateSensorDateBySensorId(LocalDateTime.now(), sensor.getId());
					if (!parkAreaUpdate) {
						parkAreaUpdate = true;
					}
				}
			}

			if (sensorUpdate) {
				logger.info("Sensor data updated");
			} else {
				logger.info("No Sensors data to update");
			}

			if (parkAreaUpdate) {
				logger.info("ParkingArea data updated");
			} else {
				logger.info("No ParkingArea data to update");
			}
			
			List<Sensor> lowBatterySensors = getLowBatterySensors(sensors);
			List<Sensor> corruptedSensors = getCorruptedSensors(sensors);
			
			String lowBattery = "";
			for(int i = 0; i < lowBatterySensors.size(); i++) {
				lowBattery += lowBatterySensors.get(i).printMail() + "\n\n";
			}
			
			String sensorOff = "";
			for(int i = 0; i < corruptedSensors.size(); i++) {
				sensorOff += corruptedSensors.get(i).printMail() + "\n\n";
			}
			
			String lowBatteryMessage = lowBatteryStartMessage + lowBattery + "\n\n" + lowBatteryEndMessage;
			String sensorOffMessage = sensorOffStartMessage + sensorOff + "\n\n" + sensorOffEndMessage;

			if (!lowBatterySensors.isEmpty() && !sentMailLB) {
				mailService.sendEmail(mailReciver, lowBatterySubject, lowBatteryMessage);
				sentMailLB = true;
			}

			if (!corruptedSensors.isEmpty() && !sentMailSO) {
				mailService.sendEmail(mailReciver, sensorOffSubject, sensorOffMessage);
				sentMailSO = true;
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

	public List<Sensor> getLowBatterySensors(MarkerList sensors) {
		List<Sensor> lowBatterySensors = new ArrayList<>();

		for (Marker m : sensors.getMarkers().getMarkers()) {
			String battery = m.getBattery().substring(0, 3).replace(',', '.');
			Float b = Float.parseFloat(battery);
			if (b < 3) {
				Sensor corruptedSensor = buildSensorFromMarker(m);
				lowBatterySensors.add(corruptedSensor);
			}
		}
		return lowBatterySensors;
	}

	public List<Sensor> getCorruptedSensors(MarkerList sensors) {
		List<Sensor> corruptedSensors = new ArrayList<>();

		for (Marker m : sensors.getMarkers().getMarkers()) {
			if (!m.isActive()) {
				Sensor corruptedSensor = buildSensorFromMarker(m);
				corruptedSensors.add(corruptedSensor);
			}
		}
		return corruptedSensors;
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
	
	public void updateSensorDateBySensorId(LocalDateTime data, Long sensorId) {
		logger.debug("ParkingService START updateSensorDateBySensorId - sensorId:{}", sensorId);
		parkingAreaRepository.updateSensorDateBySensorId(data, sensorId);
		logger.debug("ParkingService END updateParkingAreaDateById - sensorId:{}", sensorId);
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

		parkArea.setValue(marker.getState());
		
		parkArea.setLastUpdate(LocalDateTime.now());

		

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
		logger.debug("ParkingService END getParkingAreaLastUpdateDateBySensorId - sensorId:{} - date:{}", sensorId, date);
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

//	state = 0 : free, 1 : occupy
	public void updateParkingAreaValueById(boolean state, Long id) {
		logger.debug("ParkingService START updateParkingAreaValueById - parkAreaId{} - state:{}", id, state);
		parkingAreaRepository.updateValueById(state, id);
		logger.debug("ParkingService END updateParkingAreaValueById - parkAreaId{} - state:{}", id, state);
	}

//	SensorsMainteiner's Services

	public SensorsMaintainer buildSensorsMaintainerFromMarker(Marker marker) {
		logger.debug("ParkingService START buildSensorsMaintainerFromMarker");
		SensorsMaintainer sensorsMaintainer = new SensorsMaintainer();

		if (marker.getId() != null) {
			sensorsMaintainer.setFkSensorId(marker.getId());
		}
		logger.debug("ParkingService END buildSensorsMaintainerFromMarker");
		return sensorsMaintainer;
	}

	public void saveSensorsMaintainerData(SensorsMaintainer sensorsMaintainer) {

		logger.debug("ParkingService START saveSensorsMaintainerData");

		try {
			sensorsMaintainerRepository.save(sensorsMaintainer);
		} catch (Exception e) {
			logger.error("ParkingService - Error", e);
		}

		logger.debug("ParkingService END saveSensorsMaintainerData");
	}

	public void writeSensorsMaintainerData() {

		logger.debug("ParkingService START writeSensorsMaintainerData");

		try {
			MarkerList sensors = readSensorData();

			for (Marker m : sensors.getMarkers().getMarkers()) {
				SensorsMaintainer s = buildSensorsMaintainerFromMarker(m);
				saveSensorsMaintainerData(s);
			}

		} catch (Exception e) {
			logger.error("parkingService - Error", e);
		}

		logger.debug("ParkingService END writeSensorsMaintainerData");

	}

	public List<SensorsMaintainer> getSensorsMaintainerDataBySensorId(Long sensorId) {
		logger.debug("ParkingService START getSensorsMaintainerDataBySensorId");
		List<SensorsMaintainer> l = sensorsMaintainerRepository.getSensorsMaintainersByFkSensorId(sensorId);
		logger.debug("ParkingService END getSensorsMaintainerDataBySensorId");
		return l;
	}
	
	public SensorsMaintainer getSensorsMaintainerDataById(Long id) {
		logger.debug("ParkingService START getSensorsMaintainerDataById");
		SensorsMaintainer l = sensorsMaintainerRepository.getSensorsMaintainerById(id);
		logger.debug("ParkingService END getSensorsMaintainerDataById");
		return l;
	}

	public void updateSensorsMaintainerData(Maintainer maintainer, Long sensorId) {
		logger.debug("ParkingService START updateSensorsMaintainerData");
		List<SensorsMaintainer> l = getSensorsMaintainerDataBySensorId(sensorId);

		for (SensorsMaintainer m : l) {
			if (maintainer.getName() != null) {
				sensorsMaintainerRepository.updateNameById(maintainer.getName(), m.getId());
			}

			if (maintainer.getSurname() != null) {
				sensorsMaintainerRepository.updateSurnameById(maintainer.getSurname(), m.getId());
			}

			if (maintainer.getCompany() != null) {
				sensorsMaintainerRepository.updateCompanyById(maintainer.getCompany(), m.getId());
			}

			if (maintainer.getPhoneNumber() != null) {
				sensorsMaintainerRepository.updatePhoneNumberById(maintainer.getPhoneNumber(), m.getId());
			}

			if (maintainer.getMail() != null) {
				sensorsMaintainerRepository.updateMailById(maintainer.getMail(), m.getId());
			}
		}
		logger.debug("ParkingService END updateSensorsMaintainerData");
	}

	public void updateSensorsMaintainerDataById(Maintainer maintainer, Long id) {
		logger.debug("ParkingService START updateSensorsMaintainerDataById");

		if (maintainer.getName() != null) {
			sensorsMaintainerRepository.updateNameById(maintainer.getName(), id);
		}

		if (maintainer.getSurname() != null) {
			sensorsMaintainerRepository.updateSurnameById(maintainer.getSurname(), id);
		}

		if (maintainer.getCompany() != null) {
			sensorsMaintainerRepository.updateCompanyById(maintainer.getCompany(), id);
		}

		if (maintainer.getPhoneNumber() != null) {
			sensorsMaintainerRepository.updatePhoneNumberById(maintainer.getPhoneNumber(), id);
		}

		if (maintainer.getMail() != null) {
			sensorsMaintainerRepository.updateMailById(maintainer.getMail(), id);
		}
		
		logger.debug("ParkingService END updateSensorsMaintainerDataById");
	}
	
	public void updateSensorMaintainerToBeRepairedById(boolean toBeRepaired, Long id) {
		logger.debug("ParkingService START updateSensorMaintainerToBeRepaired");
		sensorsMaintainerRepository.updateToBeRepairedById(toBeRepaired, id);
		logger.debug("ParkingService END updateSensorMaintainerToBeRepaired");
	}

}
