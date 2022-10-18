package it.synclab.smartparking.service;

import java.time.LocalDateTime;
import java.time.Period;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import it.synclab.smartparking.config.datasource.PostgreClient;
import it.synclab.smartparking.model.Marker;
import it.synclab.smartparking.model.MarkerList;
import it.synclab.smartparking.repository.model.ParkingSensors;
import it.synclab.smartparking.repository.model.ParkingAreaStats;
import it.synclab.smartparking.repository.model.Sensors;
import it.synclab.smartparking.repository.model.SensorsMaintainer;

@Component
@Transactional
public class StartUpServices {
	@Value("${sensor.parking.url}")
	private String sensorDataUrl;

	@Autowired
	PostgreClient databaseClient;

	@Autowired
	private MailServices mailServices;
	
	@Autowired
	private SensorsServices sensorsServices;
	
	@Autowired
	private ParkingSensorsServices parkingsensorsServices;
	
	@Autowired
	private SensorMaintainerServices sensorMaintainerServices;
	
	@Autowired
	private ParkingStatsServices parkingStatsServices;
	
	
	
	

	private static final Logger logger = LogManager.getLogger(StartUpServices.class);

	public MarkerList convertXMLtoJson(String xml) {
		MarkerList markersList = null;
		try {
			logger.debug("StartUpServices START convertXMLtoJson");
			JSONObject jsonObj = XML.toJSONObject(xml);
			String object = jsonObj.toString();
			ObjectMapper objectMapper = new ObjectMapper();
			markersList = objectMapper.readValue(object, MarkerList.class);
		} catch (Exception e) {
			logger.error("StartUpServices - Error", e);
		}
		logger.debug("StartUpServices END convertXMLtoJson");
		return markersList;
	}

	public MarkerList readDataFromSources() throws Exception {
		logger.debug("StartUpServices START readSensorData");
		MarkerList markersList = null;
		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(sensorDataUrl).build();
			Response response = client.newCall(request).execute();
			String xmlSensorData = response.body().string();
			markersList = convertXMLtoJson(xmlSensorData);
		} catch (Exception e) {
			logger.error("StartUpServices - Error", e);
		}
		logger.debug("StartUpServices END readSensorData");
		return markersList;
	}

	// Do this every 2 minutes (Polling Function)
	@Scheduled(cron = "${polling.timer}")
	public void updateSensorsData() {
		logger.debug("StartUpServices START updateSensorsData");
		try {
			MarkerList sensors = readDataFromSources();
			writeSensorsIfAdded(sensors);
			updateDBData(sensors);
			mailServices.sendLowBatterySensorsMail();
			mailServices.sendCorruptedSensorsMail();
			mailServices.sendNotUpdatingSensorsMail();
		} catch (Exception e) {
			logger.error("StartUpServices ERROR updateSensorsData", e);
		}
		logger.debug("StartUpServices END updateSensorsData");
	}

	public void updateDBData(MarkerList sensors) {
		
		for (Marker m : sensors.getMarkers().getMarkers()) {
			boolean updated = false;
			Sensors sensor = sensorsServices.buildSensorFromMarker(m);
			ParkingSensors parkSensor = parkingsensorsServices.buildParkingSensorFromMarker(m);
			Sensors sensorFromDB = sensorsServices.getSensorById(sensor.getId());
			ParkingSensors parkingSensorFromDB = parkingsensorsServices.getParkingSensorBySensorId(parkSensor.getFkSensorId());
			//Exception when name is null!! Is it intended?
			if (!sensorFromDB.getName().equals(sensor.getName())) {
				sensorsServices.updateSensorNameById(sensor.getName(), sensor.getId());
			}
			if (!sensorFromDB.getBattery().equals(sensor.getBattery())) {
				sensorsServices.updateSensorBatteryById(sensor.getBattery(), sensor.getId());
				updated = true;
			}
			if (!sensorFromDB.getCharge().equals(sensor.getCharge())) {
				sensorsServices.updateSensorChargeById(sensor.getCharge(), sensor.getId());
				if (sensor.getCharge().equals("2") || sensor.getCharge().equals("1")) {
					sensorMaintainerServices.updateSensorMaintainerToBeChargedBySensorId(true, sensor.getId());
				}
			}
			if (!sensorFromDB.getType().equals(sensor.getType())) {
				sensorsServices.updateSensorTypeById(sensor.getType(), sensor.getId());
			}
			if (sensorFromDB.isActive() != sensor.isActive()) {
				sensorsServices.updateSensorStateById(sensor.isActive(), sensor.getId());
				updated = true;
				if (!sensor.isActive()) {
					sensorMaintainerServices.updateSensorMaintainerToBeRepairedBySensorId(true, sensor.getId());
				}
			}
			
			if(updated){
				sensorsServices.updateSensorLastSurveyById(sensor.getId());
			}

			else if(sensorFromDB.getLastSurvey().isBefore(LocalDateTime.now().minusDays(5))){
				sensorMaintainerServices.updateSensorMaintainerIsUpdatingToFalseById(sensor.getId());
				sensorsServices.updateSensorLastSurveyById(sensor.getId());
			}
			if (!parkingSensorFromDB.getLatitude().equals(parkSensor.getLatitude())) {
				// tmp.getId() because when extract data from sensor you don't
//					have p.Id because this is automatically assigned by DB
				parkingsensorsServices.updateParkingSensorLatitudeById(parkSensor.getLatitude(), parkingSensorFromDB.getId());
			}
			if (!parkingSensorFromDB.getLongitude().equals(parkSensor.getLongitude())) {
				parkingsensorsServices.updateParkingSensorLongitudeById(parkSensor.getLongitude(), parkingSensorFromDB.getId());
			}
			if (!parkingSensorFromDB.getAddress().equals(parkSensor.getAddress())) {
				parkingsensorsServices.updateParkingSensorAddressById(parkSensor.getAddress(), parkingSensorFromDB.getId());
			}
			// value = state
			if (parkingSensorFromDB.getValue() != parkSensor.getValue()) {
				parkingsensorsServices.updateParkingAreaValueById(parkSensor.getValue(), parkingSensorFromDB.getId());
				parkingsensorsServices.updateParkingSensorLastUpdateBySensorId(LocalDateTime.now(), sensor.getId());
				ParkingAreaStats stats = parkingStatsServices.buildParkingAreaStatsFromParkingArea(parkSensor);
				parkingStatsServices.saveParkingAreaStats(stats);
			}
		}
		LocalDateTime threeYearsAgo = LocalDateTime.now().minus(Period.ofYears(3));
		parkingStatsServices.deleteParkingAreaStatsBeforeDate(threeYearsAgo);
	}
	
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
	
	public void writeSensorsMaintainerData() {
		logger.debug("SensorMaintainerServices START writeSensorsMaintainerData");
		try {
			MarkerList sensors = readDataFromSources();
			for (Marker m : sensors.getMarkers().getMarkers()) {
				SensorsMaintainer maintainer = sensorMaintainerServices.buildSensorsMaintainerFromMarker(m);
				sensorMaintainerServices.saveSensorsMaintainerData(maintainer);
			}
		} catch (Exception e) {
			logger.error("SensorMaintainerServices - Error", e);
		}
		logger.debug("SensorMaintainerServices END writeSensorsMaintainerData");
	}
	
	public void writeSensorsData() {
		logger.debug("sensorsServices START writeSensorData");
		try {
			MarkerList sensors = readDataFromSources();
			for (Marker m : sensors.getMarkers().getMarkers()) {
				Sensors s = sensorsServices.buildSensorFromMarker(m);
				sensorsServices.saveSensorData(s);
			}
		} catch (Exception e) {
			logger.error("sensorsServices - Error", e);
		}
		logger.debug("sensorsServices END writeSensorData");
	}
	
	public void writeSensorsIfAdded(MarkerList sensors) {
		int markerListSize = (sensors.getMarkers().markers.size());
		int sensorListFromDB = (sensorsServices.getAllSensorsFromDB().size());
		logger.debug("Number of sensors from file:{}", markerListSize);
		logger.debug("Number of sensors from Data Base:{}", sensorListFromDB);
		if (sensorListFromDB == 0) {
			logger.debug("DB is empty.");
			writeSensorsData();
			writeSensorsMaintainerData();
		}
		else if (sensorListFromDB != markerListSize ) {
			logger.debug("there is a new sensor.");
			for (int i = sensorListFromDB; i < markerListSize; i++) {
				Marker marker = sensors.getMarkers().getMarkers().get(i);
				sensorsServices.saveSensorData(sensorsServices.buildSensorFromMarker(marker));
				sensorMaintainerServices.saveSensorsMaintainerData(sensorMaintainerServices.buildSensorsMaintainerFromMarker(marker));
			}
		}
	}
}
