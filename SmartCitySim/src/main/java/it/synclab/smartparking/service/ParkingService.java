package it.synclab.smartparking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
	private ParkingAreaRepository parkingAreaRepository;

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

	// Sensor's Services

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

	public Sensor buildSensorFromMarker(Marker marker) {
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
			e.printStackTrace();
		}
	}
	
	
	//Do this every 2 minutes
	@Scheduled(cron = "*/15 * * * * *")
	public void updateSensorsData() {

		try {
			MarkerList sensors = readSensorData();

			for (Marker m : sensors.getMarkers().getMarkers()) {
				Sensor s = buildSensorFromMarker(m);
				
				Sensor aux = sensorsRepository.getSensorById(s.getId());


				if(!aux.getName().equals(s.getName())) {
					updateSensorNameById(s.getName(), sensorsRepository.getSensorById(s.getId()).getId());
					System.out.println("Name of sensor with id = " + s.getId() + " updated in " +  s.getName());
				} else {
					System.out.println("Name not updated");
				}
				
				//TODO: continue with other controls
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	state = 0 : notWorking, 1 : working
	public boolean getSensorState(Long sensorId) {
		boolean state = sensorsRepository.getSensorState(sensorId);
		System.out.println(state);
		return state;
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

	public Sensor getSensorById(Long sensorId) {
		Sensor s = sensorsRepository.getSensorById(sensorId);
		return s;
	}

	public List<Sensor> getSensorByName(String name) {
		List<Sensor> sensors = sensorsRepository.getSensorsByName(name);
		return sensors;
	}

	public List<Sensor> getSensorByType(String type) {
		List<Sensor> sensors = sensorsRepository.getSensorsByType(type);
		return sensors;
	}

	public List<Sensor> getSensorByNameStartingWith(String str) {
		List<Sensor> sensors = sensorsRepository.getSensorByNameStartingWith(str);
		return sensors;
	}

	public List<Sensor> getSensorByNameEndingWith(String str) {
		List<Sensor> sensors = sensorsRepository.getSensorByNameEndingWith(str);
		return sensors;
	}

	public List<Sensor> getSensorByNameContaining(String str) {
		List<Sensor> sensors = sensorsRepository.getSensorByNameContaining(str);
		return sensors;
	}

	public List<Sensor> getSensorByIsActiveTrue() {
		List<Sensor> sensors = sensorsRepository.getSensorByIsActiveTrue();
		return sensors;
	}

	public List<Sensor> getSensorByIsActiveFalse() {
		List<Sensor> sensors = sensorsRepository.getSensorByIsActiveFalse();
		return sensors;
	}

	public void updateSensorNameById(String name, Long sensorId) {
		sensorsRepository.updateSensorName(name, sensorId);
	}

	public void updateSensorTypeById(String type, Long sensorId) {
		sensorsRepository.updateSensorTypeById(type, sensorId);
	}
	
	public void updateSensorStateById(boolean state, Long id) {
		sensorsRepository.updateStateById(state, id);
	}

	public void deleteSensorById(Long sensorId) {
		sensorsRepository.deleteById(sensorId);
	}

	public void deleteAlSensors() {
		sensorsRepository.deleteAll();
	}

	// ParkingArea's Services

	// Now this is useless cause we're writing ParkingArea using Sensor's db writer
	public void saveParkingSensorData() {
		try {
			ParkingArea parkArea = new ParkingArea("latitude", "longitude", "address", true);
			parkingAreaRepository.save(parkArea);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public MarkerList readParkingAreaData() throws Exception {

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

	public void saveParkingAreaData(ParkingArea parkArea) {
		try {
			parkingAreaRepository.save(parkArea);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public ParkingArea buildParkingAreaFromMarker(Marker marker) {
		ParkingArea parkArea = new ParkingArea();

		if (marker.getId() != null) {
			parkArea.setId(marker.getId());
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

		return parkArea;
	}

	public List<ParkingArea> buildListOfParkingAreaFromMarker() {
		List<ParkingArea> list = new ArrayList<>();

		try {
			MarkerList parkArea = readParkingAreaData();

			for (Marker m : parkArea.getMarkers().getMarkers()) {
				ParkingArea p = new ParkingArea();
				if (m.getId() != null) {
					p.setId(m.getId());
				}

				if (m.getLat() != null) {
					p.setLatitude(m.getLat());
				}

				if (m.getLng() != null) {
					p.setLongitude(m.getLng());
				}

				if (m.getAddress() != null) {
					p.setAddress(m.getAddress());
				}

				list.add(p);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	// This is never used
	public void writeParkingAreaData() {

		try {
			MarkerList parkArea = readParkingAreaData();

			for (Marker m : parkArea.getMarkers().getMarkers()) {
				ParkingArea s = buildParkingAreaFromMarker(m);
				saveParkingAreaData(s);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ParkingArea getParkingAreaByYd(Long id) {
		ParkingArea p = parkingAreaRepository.getParkingAreaById(id);
		return p;
	}

	public String getParkingAreaLatitudeById(Long id) {
		String latitude = parkingAreaRepository.getLatitudeById(id);
		return latitude;

	}

	public String getParkingAreaLongitudeById(Long id) {
		String longitude = parkingAreaRepository.getLongitudeById(id);
		return longitude;
	}

	public String getParkingAreaAddressById(Long id) {
		String address = parkingAreaRepository.getAddressById(id);
		return address;

	}

	public boolean getParkingAreaStateById(Long id) {
		boolean state = parkingAreaRepository.getStateById(id);
		return state;

	}

	public List<ParkingArea> getFreeParkingArea() {
		List<ParkingArea> state = parkingAreaRepository.getParkingAreaByValueTrue();
		return state;
	}

	public List<ParkingArea> getOccupyParkingArea() {
		List<ParkingArea> state = parkingAreaRepository.getParkingAreaByValueFalse();
		return state;
	}

//	it works but for now we have more sensor in data set with equals value for 
//	both latitude and longitude so this returns a List of ParkingArea
//	public ParkingArea getParkingAreaByLatitudeAndLongitude(String latitude, String longitude) {
//		ParkingArea p = parkingAreaRepository.getParkingAreaByLatitudeAndLongitude(latitude, longitude);
//		return p;
//	}

	public void updateParkingAreaLatitudeById(String latitude, Long id) {
		parkingAreaRepository.updateLatitudeById(latitude, id);
	}

	public void updateParkingAreaLongitudeById(String longitude, Long id) {
		parkingAreaRepository.updateLongitudeById(longitude, id);
	}

	public void updateParkingAreaAddressById(String address, Long id) {
		parkingAreaRepository.updateAddressById(address, id);
	}

//	state = 0 : free, 1 : occupy
	public void updateParkingAreaStateById(boolean state, Long id) {
		parkingAreaRepository.setStateById(state, id);
	}
}
