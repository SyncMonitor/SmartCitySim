package it.synclab.smartparking.service;

import java.time.LocalDateTime;
import java.time.Period;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import it.synclab.smartparking.datasource.config.PostgreClient;
import it.synclab.smartparking.model.Marker;
import it.synclab.smartparking.model.MarkerList;
import it.synclab.smartparking.repository.model.ParkingArea;
import it.synclab.smartparking.repository.model.ParkingAreaStats;
import it.synclab.smartparking.repository.model.Sensor;
import it.synclab.smartparking.repository.model.SensorsMaintainer;

@Component
@Transactional
public class StartUpServices {
	@Value("${sensor.parking.url}")
	private String sensorDataUrl;

	@Autowired
	PostgreClient databaseClient;

//	@Autowired
//	DataSource databaseClient;

	@Autowired
	private MailServices mailServices;
	
	@Autowired
	private SensorServices sensorServices;
	
	@Autowired
	private ParkingAreaServices parkingAreaServices;
	
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
		} catch (Exception e) {
			logger.error("StartUpServices ERROR updateSensorsData", e);
		}
		logger.debug("StartUpServices END updateSensorsData");
	}

	public void updateDBData(MarkerList sensors) {
	
		for (Marker m : sensors.getMarkers().getMarkers()) {
			Sensor sensor = sensorServices.buildSensorFromMarker(m);
			ParkingArea parkArea = parkingAreaServices.buildParkingAreaFromMarker(m);
			Sensor sensorFromDB = sensorServices.getSensorById(sensor.getId());
			ParkingArea parkingAreaFromDB = parkingAreaServices.getParkingAreaBySensorId(parkArea.getFkSensorId());
			if (!sensorFromDB.getName().equals(sensor.getName())) {
				sensorServices.updateSensorNameById(sensor.getName(), sensor.getId());
			}
			if (!sensorFromDB.getBattery().equals(sensor.getBattery())) {
				sensorServices.updateSensorBatteryById(sensor.getBattery(), sensor.getId());
			}
			if (!sensorFromDB.getCharge().equals(sensor.getCharge())) {
				sensorServices.updateSensorChargeById(sensor.getCharge(), sensor.getId());
				if (sensor.getCharge().equals("2") || sensor.getCharge().equals("1")) {
					sensorMaintainerServices.updateSensorMaintainerToBeChargedBySensorId(true, sensor.getId());
				}
			}
			if (!sensorFromDB.getType().equals(sensor.getType())) {
				sensorServices.updateSensorTypeById(sensor.getType(), sensor.getId());
			}
			if (sensorFromDB.isActive() != sensor.isActive()) {
				sensorServices.updateSensorStateById(sensor.isActive(), sensor.getId());
				if (!sensor.isActive()) {
					sensorMaintainerServices.updateSensorMaintainerToBeRepairedBySensorId(true, sensor.getId());
				}
			}
			if (!parkingAreaFromDB.getLatitude().equals(parkArea.getLatitude())) {
				// tmp.getId() because when extract data from sensor you don't
//					have p.Id because this is automatically assigned by DB
				parkingAreaServices.updateParkingAreaLatitudeById(parkArea.getLatitude(), parkingAreaFromDB.getId());
			}
			if (!parkingAreaFromDB.getLongitude().equals(parkArea.getLongitude())) {
				parkingAreaServices.updateParkingAreaLongitudeById(parkArea.getLongitude(), parkingAreaFromDB.getId());
			}
			if (!parkingAreaFromDB.getAddress().equals(parkArea.getAddress())) {
				parkingAreaServices.updateParkingAreaAddressById(parkArea.getAddress(), parkingAreaFromDB.getId());
			}
			// value = state
			if (parkingAreaFromDB.getValue() != parkArea.getValue()) {
				parkingAreaServices.updateParkingAreaValueById(parkArea.getValue(), parkingAreaFromDB.getId());
				parkingAreaServices.updateParkingAreaLastUpdateBySensorId(LocalDateTime.now(), sensor.getId());
				ParkingAreaStats stats = parkingStatsServices.buildParkingAreaStatsFromParkingArea(parkArea);
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
		logger.debug("SensorServices START writeSensorData");
		try {
			MarkerList sensors = readDataFromSources();
			for (Marker m : sensors.getMarkers().getMarkers()) {
				Sensor s = sensorServices.buildSensorFromMarker(m);
				sensorServices.saveSensorData(s);
			}
		} catch (Exception e) {
			logger.error("SensorServices - Error", e);
		}
		logger.debug("SensorServices END writeSensorData");
	}
	
	public void writeSensorsIfAdded(MarkerList sensors) {
		int markerListSize = (sensors.getMarkers().markers.size());
		int sensorListFromDB = (sensorServices.getAllSensorsFromDB().size());
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
				sensorServices.saveSensorData(sensorServices.buildSensorFromMarker(marker));
				sensorMaintainerServices.saveSensorsMaintainerData(sensorMaintainerServices.buildSensorsMaintainerFromMarker(marker));
			}
		}
	}
}
