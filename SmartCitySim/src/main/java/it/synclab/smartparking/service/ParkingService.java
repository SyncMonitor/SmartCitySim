package it.synclab.smartparking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.List;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.synclab.smartparking.datasource.config.PostgreClient;
import it.synclab.smartparking.model.Marker;
import it.synclab.smartparking.model.MarkerList;
import it.synclab.smartparking.repository.ParkingAreaRepository;
import it.synclab.smartparking.repository.SensorsRepository;
import it.synclab.smartparking.repository.model.ParkingArea;
import it.synclab.smartparking.repository.model.Sensor;

@Component
public class ParkingService {

	private final String sensorDataUrl = "https://syncmonitor.altervista.org/smartparking/test1.xml";

	@Autowired
	PostgreClient databaseClient;

	@Autowired
	private SensorsRepository sensorsRepository;

	@Autowired
	private ParkingAreaRepository parkingSensorsRepository;

	public MarkerList convertXMLtoJson(String xml) {

		MarkerList markersList = null;

		try {
			JSONObject jsonObj = XML.toJSONObject(xml);
			String object = jsonObj.toString();
			ObjectMapper objectMapper = new ObjectMapper();
			markersList = objectMapper.readValue(object, MarkerList.class);

		} catch (Exception e) {
			System.out.println(e);
		}

		return markersList;
	}

	/*
	 * 0 free 1 occupy
	 **/
	public int getSensorState(String sensorId) {
		// TODO: Read data from database
		return 0;
	}

	public MarkerList readSensorData() throws Exception {

		MarkerList markersList = null;

		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(this.sensorDataUrl).build();
			Response response = client.newCall(request).execute();
			String xmlSensorData = response.body().string();
			markersList = convertXMLtoJson(xmlSensorData);
		} catch (Exception e) {
			System.out.println(e);
		}

		return markersList;
	}
	
	public void saveSensorData(Sensor sensor) {
		try {
			sensorsRepository.save(sensor);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public List<Sensor> getSensorsByName(String name) {
		List<Sensor> sensors = sensorsRepository.findByName(name);
		try {
			System.out.println("Sensori trovati: " + sensors);
		} catch (Exception e) {
			System.out.println(e);
		}
		return sensors;
	}

	public Sensor getSensorsById(int sensorId) {
		Sensor s = sensorsRepository.getById(sensorId);
		return s;
	}

	public void writeSensorsData() {

		try {
			MarkerList sensors = readSensorData();

			for (Marker m : sensors.getMarkers().getMarkers()) {
				Sensor s = buildSensorFromMarker(m);
				saveSensorData(s);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// readSensorData(sensore);

		// ciclo for sulla lista ottenuta da readSensorData()

		// per ogni oggetto Sensor chiama saveSensorData()

		// popolare parkingArea

	}

	// Fare metodo che converte Marker in Sensor

	public Sensor buildSensorFromMarker(Marker m) {
		Sensor s = new Sensor();

		if (m.getBattery() != null) {
			s.setBattery(m.getBattery());
		}

		// TOCOMPLETE

		return s;
	}

	public void saveParkingSensorData() {
		try {
			ParkingArea parkArea = new ParkingArea("latitude", "longitude", "address", "value");
			parkingSensorsRepository.save(parkArea);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public ParkingArea buildParkingAreaFromMarker(Marker m) {
		// TODO
		return null;
	}

}
