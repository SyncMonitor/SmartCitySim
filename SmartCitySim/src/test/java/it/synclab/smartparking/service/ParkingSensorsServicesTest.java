package it.synclab.smartparking.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import it.synclab.smartparking.model.Marker;
import it.synclab.smartparking.repository.model.ParkingSensors;
import it.synclab.smartparking.repository.model.Sensors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParkingSensorsServicesTest {

	@Autowired
	private ParkingSensorsServices parkingAreaServices;

	@Autowired
	private SensorsServices sensorsServices;

	ParkingSensors parkingArea = new ParkingSensors();
	Sensors sensor;
	Marker marker = new Marker();
	int size = 0;

	@Before
	public void init() {
		parkingArea = new ParkingSensors("45.00000", "24.00000", "Sensor address", false, null);

		marker.setId(99999999L);
		marker.setName("sensor 97");
		marker.setAddress("Via sensor 97");
		marker.setLat("45.00000");
		marker.setLng("24.00000");
		marker.setState(false);
		marker.setBattery("3,5V");
		marker.setActive(true);

		sensor = sensorsServices.buildSensorFromMarker(marker);

		size = parkingAreaServices.getAllParkingSensors().size();

	}

	@Transactional
	@Test
	public void getAllParkingSensorsTest() {
		sensorsServices.saveSensorData(sensor);
		List<ParkingSensors> parkingAreas = parkingAreaServices.getAllParkingSensors();
		Assert.assertTrue(parkingAreas.size() > 0);
	}

	@Test
	public void buildParkingSensorFromMarkerTest() {

		ParkingSensors parkArea = parkingAreaServices.buildParkingSensorFromMarker(marker);

		Assert.assertEquals(parkArea.getAddress(), marker.getAddress());
		Assert.assertEquals(parkArea.getLatitude(), marker.getLat());
		Assert.assertEquals(parkArea.getLongitude(), marker.getLng());
		Assert.assertEquals(parkArea.getValue(), marker.getState());

		marker.setId(null);
		marker.setAddress(null);
		marker.setLat(null);
		marker.setLng(null);

		parkArea = parkingAreaServices.buildParkingSensorFromMarker(marker);

		Assert.assertEquals(null, parkingArea.getId());
	}

	@Test
	@Transactional
	public void getParkingSensorByIdTest() {
		sensorsServices.saveSensorData(sensor);
		Sensors fromDB = sensorsServices.getSensorById(sensor.getId());
		Long id = fromDB.getParkingSensors().getId();
		ParkingSensors result = parkingAreaServices.getParkingSensorById(id);
		Assert.assertEquals(id, result.getId());
	}

	@Transactional
	@Test
	public void getParkingSensorBySensorIdTest() {
		sensorsServices.saveSensorData(sensor);
		Sensors fromDB = sensorsServices.getSensorById(sensor.getId());
		Long sensorId = fromDB.getId();
		ParkingSensors result = parkingAreaServices.getParkingSensorBySensorId(sensorId);
		Assert.assertEquals(sensorId, result.getFkSensorId());
	}

	@Test
	@Transactional
	public void getParkingSensorLatitudeByIdTest() {
		sensorsServices.saveSensorData(sensor);
		Sensors fromDB = sensorsServices.getSensorById(sensor.getId());
		Long id = fromDB.getParkingSensors().getId();
		String latitude = parkingAreaServices.getParkingSensorLatitudeById(id);
		Assert.assertEquals(latitude, sensor.getParkingSensors().getLatitude());
	}

	@Test
	@Transactional
	public void getParkingSensorLongitudeByIdTest() {
		sensorsServices.saveSensorData(sensor);
		Sensors fromDB = sensorsServices.getSensorById(sensor.getId());
		Long id = fromDB.getParkingSensors().getId();
		String longitude = parkingAreaServices.getParkingSensorLongitudeById(id);
		Assert.assertEquals(longitude, sensor.getParkingSensors().getLongitude());
	}

	@Test
	@Transactional
	public void getParkingSensorAddressByIdTest() {
		sensorsServices.saveSensorData(sensor);
		Sensors fromDB = sensorsServices.getSensorById(sensor.getId());
		Long id = fromDB.getParkingSensors().getId();
		String address = parkingAreaServices.getParkingSensorAddressById(id);
		Assert.assertEquals(address, sensor.getParkingSensors().getAddress());
	}

	@Test
	@Transactional
	public void getParkingSensorStateByIdTest() {
		sensorsServices.saveSensorData(sensor);
		Sensors fromDB = sensorsServices.getSensorById(sensor.getId());
		Long id = fromDB.getParkingSensors().getId();
		boolean state = parkingAreaServices.getParkingSensorStateById(id);
		Assert.assertEquals(state, sensor.getParkingSensors().getValue());
	}

	/*@Test
	@Transactional
	public void getFreeParkingAreaTest() {
		sensorsServices.deleteAllSensors();
		parkingAreaServices.deleteAllParkingSensors();
		List<ParkingSensors> parkArea = new ArrayList<>();
		parkArea.add(parkingArea);
		for (int i = 0; i < 4; i++) {
			Sensors sensor = new Sensors(i + 1L, "Sensor " + i, "3,7V", "3", "ParkingArea", true, parkArea);
			sensorsServices.saveSensorData(sensor);
		}
		parkingArea.setValue(true);
		for (int i = 0; i < 5; i++) {
			Sensors sensor = new Sensors(i + 5L, "Sensor " + i, "3,7V", "3", "ParkingArea", true, parkArea);
			sensorsServices.saveSensorData(sensor);
		}
		List<ParkingSensors> result = parkingAreaServices.getFreeParkingSensors();
		Assert.assertEquals(4, result.size());
	}

	@Test
	@Transactional
	public void getOccupyParkingAreaTest() {
		sensorsServices.deleteAllSensors();
		parkingAreaServices.deleteAllParkingSensors();
		List<ParkingSensors> parkArea = new ArrayList<>();
		parkArea.add(parkingArea);
		for (int i = 0; i < 4; i++) {
			Sensors sensor = new Sensors(i + 1L, "Sensor " + i, "3,7V", "3", "ParkingArea", true, parkArea);
			sensorsServices.saveSensorData(sensor);
		}
		parkingArea.setValue(true);
		for (int i = 0; i < 5; i++) {
			Sensors sensor = new Sensors(i + 5L, "Sensor " + i, "3,7V", "3", "ParkingArea", true, parkArea);
			sensorsServices.saveSensorData(sensor);
		}
		List<ParkingSensors> result = parkingAreaServices.getOccupyParkingSensors();
		Assert.assertEquals(5, result.size());
	}*/

	@Test
	@Transactional
	public void getParkingSensorByLatitudeAndLongitudeTest() {
		sensor.getParkingSensors().setLatitude("TestLatitude");
		sensor.getParkingSensors().setLongitude("TestLongitude");
		sensorsServices.saveSensorData(sensor);
		String latitude = sensor.getParkingSensors().getLatitude();
		String longitude = sensor.getParkingSensors().getLongitude();
		ParkingSensors fromDB = parkingAreaServices.getParkingSensorByLatitudeAndLongitude(latitude, longitude);
		Assert.assertEquals(latitude, fromDB.getLatitude());
		Assert.assertEquals(longitude, fromDB.getLongitude());
	}

	@Test
	@Transactional
	public void getParkingSensorLastUpdateDateBySensorIdTest() {
		sensor.getParkingSensors().setTimestamp(LocalDateTime.of(1997, 11, 30, 0, 0));
		sensorsServices.saveSensorData(sensor);
		LocalDateTime lastUpdate = sensor.getParkingSensors().getTimestamp();
		LocalDateTime result = parkingAreaServices.getParkingSensorLastUpdateDateBySensorId(sensor.getId());
		Assert.assertEquals(lastUpdate, result);
	}

	@Test
	@Transactional
	public void deleteParkingSensorBySensorIdTest() {
		sensorsServices.saveSensorData(sensor);
		int size = parkingAreaServices.getAllParkingSensors().size();
		Long sensorId = sensor.getId();
		parkingAreaServices.deleteParkingSensorBySensorId(sensorId);
		Assert.assertEquals(size - 1, parkingAreaServices.getAllParkingSensors().size());
	}

	@Test
	@Transactional
	public void deleteParkingSensorByIdTest() {
		int size = parkingAreaServices.getAllParkingSensors().size();
		sensorsServices.saveSensorData(sensor);
		Assert.assertEquals(size + 1, parkingAreaServices.getAllParkingSensors().size());
		Long id = sensorsServices.getSensorById(sensor.getId()).getParkingSensors().getId();
		parkingAreaServices.deleteParkingSensorById(id);
		Assert.assertEquals(size, parkingAreaServices.getAllParkingSensors().size());
	}

	@Test
	@Transactional
	public void deleteAllParkingSensorsTest() {
		int size = parkingAreaServices.getAllParkingSensors().size();
		if (size > 0) {
			parkingAreaServices.deleteAllParkingSensors();
			size = parkingAreaServices.getAllParkingSensors().size();
		}
		Assert.assertEquals(0, size);
	}

}