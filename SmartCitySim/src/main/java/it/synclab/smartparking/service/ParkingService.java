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
import org.springframework.transaction.annotation.Transactional;

import it.synclab.smartparking.datasource.config.PostgreClient;
import it.synclab.smartparking.model.Maintainer;
import it.synclab.smartparking.model.Marker;
import it.synclab.smartparking.model.MarkerList;
import it.synclab.smartparking.repository.ParkingAreaRepository;
import it.synclab.smartparking.repository.ParkingAreaStatsRepository;
import it.synclab.smartparking.repository.SensorsMaintainerRepository;
import it.synclab.smartparking.repository.SensorsRepository;
import it.synclab.smartparking.repository.model.ParkingArea;
import it.synclab.smartparking.repository.model.ParkingAreaStats;
import it.synclab.smartparking.repository.model.Sensor;
import it.synclab.smartparking.repository.model.SensorsMaintainer;

@Component
@Transactional
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

	@Autowired
	private SensorsRepository sensorsRepository;

	@Autowired
	private ParkingAreaRepository parkingAreaRepository;

	@Autowired
	private SensorsMaintainerRepository sensorsMaintainerRepository;

	@Autowired
	private ParkingAreaStatsRepository parkingAreaStatsRepository;

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

		if (marker.getBattery() != null) {
			s.setCharge(marker.getBattery().substring(0, 1));
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

	// Do this every 2 minutes (Polling Function)
	@Scheduled(cron = "${polling.timer}")
	public void updateSensorsData() {

		logger.debug("ParkingService START updateSensorsData");

		try {
			MarkerList sensors = readSensorData();

			writeSensorsIfAddes(sensors);

			updateDBData(sensors);

			sendLowBatterySensorsMail();
			sendCorruptedSensorsMail();

		} catch (Exception e) {
			logger.error("ParkingService ERROR updateSensorsData", e);
		}
		logger.debug("ParkingService END updateSensorsData");
	}

	public void updateDBData(MarkerList sensors) {

		boolean sensorUpdate = false;
		boolean parkAreaUpdate = false;

		for (Marker m : sensors.getMarkers().getMarkers()) {
			Sensor sensor = buildSensorFromMarker(m);
			ParkingArea parkArea = buildParkingAreaFromMarker(m);

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
				if (!sensorUpdate) {
					sensorUpdate = true;
				}
			}
			if (!sensorFromDB.getCharge().equals(sensor.getCharge())) {
				updateSensorChargeById(sensor.getCharge(), sensor.getId());
				if (sensor.getCharge().equals("2") || sensor.getCharge().equals("1")) {
					updateSensorMaintainerToBeChargedBySensorId(true, sensor.getId());
				}
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

				if (!sensor.isActive()) {
					updateSensorMaintainerToBeRepairedBySensorId(true, sensor.getId());
				}
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

			// value = state
			if (parkingAreaFromDB.getValue() != parkArea.getValue()) {
				updateParkingAreaValueById(parkArea.getValue(), parkingAreaFromDB.getId());
				updateSensorDateById(LocalDateTime.now(), sensor.getId());

				ParkingAreaStats stats = buildParkingAreaStatsFromParkingArea(parkArea);
				saveParkingAreaStats(stats);

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
	}

	public void sendCorruptedSensorsMail() {
		List<SensorsMaintainer> maintainers;
		maintainers = getAllSensorsMaintainerData();
		for (SensorsMaintainer m : maintainers) {
			if (m.isToBeRepaired()) {
				{
					Sensor s = getSensorById(m.getFkSensorId());
					logger.debug("mail: " + m.getMail());
					String sensorOff = "";
					sensorOff += printMail(s, s.getParkingArea()) + "\n\n";
					String sensorOffMessage = sensorOffStartMessage + sensorOff + sensorOffEndMessage;
					mailService.sendEmail(m.getMail(), sensorOffSubject + " " + s.getName(), sensorOffMessage);
					m.setToBeRepaired(false);
				}
			}
		}
	}

	public void sendLowBatterySensorsMail() {
		List<SensorsMaintainer> maintainers;
		maintainers = getAllSensorsMaintainerData();
		for (SensorsMaintainer m : maintainers) {
			if (m.isToBeCharged()) {
				{
					Sensor s = getSensorById(m.getFkSensorId());
					logger.debug("mail: " + m.getMail());
					String lowBattery = "";
					lowBattery += printMail(s, s.getParkingArea()) + "\n\n";
					String lowBatterySensorMessage = lowBatteryStartMessage + lowBattery + lowBatteryEndMessage;
					mailService.sendEmail(m.getMail(), lowBatterySubject + " " + s.getName(), lowBatterySensorMessage);
					m.setToBeCharged(false);
				}
			}
		}
	}

	public String printMail(Sensor s, List<ParkingArea> parkingArea) {
		String text = "Id = " + s.getId() + ", Name = " + s.getName() + ", ";
		for (int i = 0; i < parkingArea.size(); i++) {
			text += "Address = " + parkingArea.get(i).getAddress() + ", Latitude = " + parkingArea.get(i).getLatitude()
					+ ", Longitude = " + parkingArea.get(i).getLongitude();
		}
		return text;
	}

	public void writeSensorsIfAddes(MarkerList sensors) {
		int markerListSize = (sensors.getMarkers().markers.size());
		int sensorListFromDB = (sensorsRepository.getAllSensors().size());

		logger.debug("Number of sensors from file:{}", markerListSize);

		logger.debug("Number of sensors from Data Base:{}", sensorListFromDB);
		if (sensorListFromDB == 0) {
			logger.debug("DB is empty.");
			writeSensorsData();
			writeSensorsMaintainerData();
		}

		else if (markerListSize != sensorListFromDB) {
			logger.debug("there is a new sensor.");
			for (int i = sensorListFromDB; i < markerListSize; i++) {
				Marker marker = sensors.getMarkers().getMarkers().get(i);
				saveSensorData(buildSensorFromMarker(marker));
				saveSensorsMaintainerData(buildSensorsMaintainerFromMarker(marker));
			}
		}
	}
	
	public List<Sensor> getAllSensorsFromDB(){
		logger.debug("ParkingService START getAllSensorsFromDB");
		List<Sensor> sensors = sensorsRepository.getAllSensorFromDB();
		logger.debug("ParkingService END getAllSensorsFromDB");
		return sensors;
	}

	// state = 0 : notWorking, 1 : working
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

	public Sensor getSensorById(Long i) {
		logger.debug("ParkingService START getSensorById - sensorId:{}", i);
		Sensor s = sensorsRepository.getSensorById(i);
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

	public void updateSensorChargeById(String charge, Long sensorId) {
		logger.debug("ParkingService START updateSensorChargeById - charge:{} - sensorId:{}", charge, sensorId);
		sensorsRepository.updateSensorChargeById(charge, sensorId);
		logger.debug("ParkingService END updateSensorChargeById - charge:{} - sensorId:{}", charge, sensorId);
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

	public void updateSensorDateById(LocalDateTime data, Long id) {
		logger.debug("ParkingService START updateSensorDateById - id:{}", id);
		parkingAreaRepository.updateSensorDateBySensorId(data, id);
		logger.debug("ParkingService END updateSensorDateById - id:{}", id);
	}

	public void deleteSensorById(Long sensorId) {
		logger.debug("ParkingService START deleteSensorById - sensorId:{}", sensorId);
		sensorsRepository.deleteById(sensorId);
		logger.debug("ParkingService END deleteSensorById - sensorId:{}", sensorId);

	}

	public void deleteAllSensors() {
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

//	SensorsMainteiner's Services

	public SensorsMaintainer buildSensorsMaintainerFromMarker(Marker marker) {
		logger.debug("ParkingService START buildSensorsMaintainerFromMarker");
		SensorsMaintainer sensorsMaintainer = new SensorsMaintainer();

		if (marker.getId() != null) {
			sensorsMaintainer.setFkSensorId(marker.getId());
		}

//		Type attribute missing in XML file
//		if(m.getType() != null) {}
		sensorsMaintainer.setType("ParkingArea");

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
				SensorsMaintainer maintainer = buildSensorsMaintainerFromMarker(m);
				saveSensorsMaintainerData(maintainer);
			}

		} catch (Exception e) {
			logger.error("parkingService - Error", e);
		}

		logger.debug("ParkingService END writeSensorsMaintainerData");

	}

	public List<SensorsMaintainer> getAllSensorsMaintainerData() {
		logger.debug("ParkingService START getAllSensorsMaintainerData");
		List<SensorsMaintainer> maintainers = sensorsMaintainerRepository.getAllSensorsMaintainerData();
		logger.debug("ParkingService END getAllSensorsMaintainerData");
		return maintainers;
	}

	public List<SensorsMaintainer> getSensorsMaintainerDataBySensorId(Long sensorId) {
		logger.debug("ParkingService START getSensorsMaintainerDataBySensorId");
		List<SensorsMaintainer> maintainers = sensorsMaintainerRepository.getSensorsMaintainersByFkSensorId(sensorId);
		logger.debug("ParkingService END getSensorsMaintainerDataBySensorId");
		return maintainers;
	}

	public SensorsMaintainer getSensorsMaintainerDataById(Long id) {
		logger.debug("ParkingService START getSensorsMaintainerDataById");
		SensorsMaintainer maintainers = sensorsMaintainerRepository.getSensorsMaintainerById(id);
		logger.debug("ParkingService END getSensorsMaintainerDataById");
		return maintainers;
	}

	public void updateSensorsMaintainerDataBySensorId(Maintainer maintainer, Long sensorId) {
		logger.debug("ParkingService START updateSensorsMaintainerDataBySensorId");
		List<SensorsMaintainer> maintainers = getSensorsMaintainerDataBySensorId(sensorId);

		for (SensorsMaintainer m : maintainers) {
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
		logger.debug("ParkingService END updateSensorsMaintainerDataBySensorId");
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

	public void updateSensorMaintainerToBeRepairedBySensorId(boolean toBeRepaired, Long sensorId) {
		logger.debug("ParkingService START updateSensorMaintainerToBeRepairedBySensorId");
		sensorsMaintainerRepository.updateToBeRepairedBySensorId(toBeRepaired, sensorId);
		logger.debug("ParkingService END updateSensorMaintainerToBeRepairedBySensorId");
	}

	public void updateSensorMaintainerToBeChargedBySensorId(boolean toBeCharged, Long sensorId) {
		logger.debug("ParkingService START updateSensorMaintainerToBeChargeddBySensorId");
		sensorsMaintainerRepository.updateToBeChargedBySensorId(toBeCharged, sensorId);
		logger.debug("ParkingService END updateSensorMaintainerToBeChargeddBySensorId");
	}
	
	public void deleteSensorMaintainersBySensorId(Long sensorId) {
		logger.debug("ParkingService deleteSensorMaintainersBySensorId deleteAlSensors ");
		sensorsMaintainerRepository.deleteSensorMaintainersBySensorId(sensorId);
		logger.debug("ParkingService END deleteSensorMaintainersBySensorId ");		
	}
	
	public void deleteSensorMaintainersById(Long id) {
		logger.debug("ParkingService START deleteSensorMaintainersById ");
		sensorsMaintainerRepository.deleteSensorMaintainersById(id);
		logger.debug("ParkingService END deleteSensorMaintainersById ");		
	}
	
	public void deleteAllSensorMaintainers() {
		logger.debug("ParkingService START deleteAlSensors ");
		sensorsMaintainerRepository.deleteAll();
		logger.debug("ParkingService END deleteAlSensors ");
	}

//	ParkingAreaStats

	public ParkingAreaStats buildParkingAreaStatsFromParkingArea(ParkingArea parkArea) {
		logger.debug("ParkingService START buildParkingAreaStatsFromParkingArea");
		ParkingAreaStats stats = new ParkingAreaStats();

		if (parkArea.getFkSensorId() != null) {
			stats.setFkSensorId(parkArea.getFkSensorId());
		}

		if (parkArea.getLastUpdate() != null) {
			stats.setLastUpdate(parkArea.getLastUpdate());
		}

		logger.debug("ParkingService END buildParkingAreaStatsFromParkingArea");

		stats.setValue(parkArea.getValue());

		return stats;
	}

	public void saveParkingAreaStats(ParkingAreaStats stats) {

		logger.debug("ParkingService START saveParkingAreaStats");

		try {
			parkingAreaStatsRepository.save(stats);
		} catch (Exception e) {
			logger.error("ParkingService - Error", e);
		}

		logger.debug("ParkingService END saveParkingAreaStats");
	}
	
	public List<ParkingAreaStats> getParkingAreaStats() {
		logger.debug("ParkingService START getParkingAreaStats");
		List<ParkingAreaStats> stats = parkingAreaStatsRepository.getParkingAreaStats();
		logger.debug("ParkingService END getParkingAreaStats");
		return stats;
	}

	public ParkingAreaStats getParkingAreaStatsById(Long id) {
		logger.debug("ParkingService START getParkingAreaStatsBySensorId");
		ParkingAreaStats stat = parkingAreaStatsRepository.getParkingAreaStatsById(id);
		logger.debug("ParkingService END getParkingAreaStatsBySensorId");
		return stat;
	}

	public List<ParkingAreaStats> getParkingAreaStatsBySensorId(Long sensorId) {
		logger.debug("ParkingService START getParkingAreaStatsBySensorId");
		List<ParkingAreaStats> stats = parkingAreaStatsRepository.getParkingAreaStatsBySensorId(sensorId);
		logger.debug("ParkingService END getParkingAreaStatsBySensorId");
		return stats;
	}

	public List<ParkingAreaStats> getParkingAreaStatsFromData(LocalDateTime data) {
		logger.debug("ParkingService START getParkingAreaStatsFromData");
		List<ParkingAreaStats> stats = parkingAreaStatsRepository.getParkingAreaStatsFromData(data);
		logger.debug("ParkingService END getParkingAreaStatsFromData");
		return stats;
	}

//	public List<ParkingAreaStats> getParkingAreaStatsFromDataToData(LocalDateTime startData, LocalDateTime endData) {
//		logger.debug("ParkingService START getParkingAreaStatsFromDataToData");
//		List<ParkingAreaStats> stats = parkingAreaStatsRepository.getParkingAreaStatsFromDataToData(startData, endData);
//		logger.debug("ParkingService END getParkingAreaStatsFromDataToData");
//		return stats;
//	}

//	public List<ParkingAreaStats> getParkingAreaStatsBySensorIdFromData(Long id, LocalDateTime data) {
//		logger.debug("ParkingService START getParkingAreaStatsBySensorIdFromData");
//		List<ParkingAreaStats> stats = parkingAreaStatsRepository.getParkingAreaStatsBySensorIdFromData(id, data);
//		logger.debug("ParkingService END getParkingAreaStatsBySensorIdFromData");
//		return stats;
//	}

//	public List<ParkingAreaStats> getParkingAreaStatsBySensorIdFromDataToData(Long id, LocalDateTime startData,
//			LocalDateTime endData) {
//		logger.debug("ParkingService START getParkingAreaStatsBySensorIdFromDataToData");
//		List<ParkingAreaStats> stats = parkingAreaStatsRepository.getParkingAreaStatsBySensorIdFromDataToData(id,
//				startData, endData);
//		logger.debug("ParkingService END getParkingAreaStatsBySensorIdFromDataToData");
//		return stats;
//	}
	
//	public void deleteParkingAreaStatsBeforeDate(LocalDateTime data) {
//		logger.debug("ParkingService START deleteParkingAreaStatsBeforeDate");
//		parkingAreaStatsRepository.deleteParkingAreaStatsBeforeDate(data);
//		logger.debug("ParkingService END deleteParkingAreaStatsBeforeDate");
//	}

	public void deleteParkingAreaStatsById(Long id) {
		logger.debug("ParkingService START deleteParkingAreaStatsById");
		parkingAreaStatsRepository.deleteParkingAreaStatsById(id);
		logger.debug("ParkingService END deleteParkingAreaStatsById");
	}
	
	public void deleteParkingAreaStatsBySensorId(Long sensorId) {
		logger.debug("ParkingService START deleteParkingAreaStatsBySensorId");
		parkingAreaStatsRepository.deleteParkingAreaStatsBySensorId(sensorId);
		logger.debug("ParkingService END deleteParkingAreaStatsBySensorId");
	}
	
	public void deleteAllParkingStats() {
		logger.debug("ParkingService START deleteAlSensors ");
		parkingAreaStatsRepository.deleteAll();
		logger.debug("ParkingService END deleteAlSensors ");
	}
}
