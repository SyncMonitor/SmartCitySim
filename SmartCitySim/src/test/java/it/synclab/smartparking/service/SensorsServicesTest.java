package it.synclab.smartparking.service;

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
import it.synclab.smartparking.model.MarkerList;
import it.synclab.smartparking.model.Markers;
import it.synclab.smartparking.repository.model.ParkingSensors;
import it.synclab.smartparking.repository.model.Sensors;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class SensorsServicesTest {

	@Autowired
	private SensorsServices sensorsServices;

	Sensors sensor = null;
	Sensors inactiveSensor = null;
	List<ParkingSensors> parkingArea = new ArrayList<>();
	ParkingSensors parkingSensor;
	ParkingSensors inactiveParkingSensor;
	List<ParkingSensors> InactiveparkingArea = new ArrayList<>();
	Marker marker = new Marker();
	int size = 0;


	@Before
	public void init() {
		parkingSensor = new ParkingSensors("45.12564", "24.65489", "Via Indirizzo di prova 100", true, null);
		sensor = new Sensors(size + 1000L, "sensor 109109109", "3,7V", "3", "ParkingArea", true, null);
		sensor.setParkingSensors(parkingSensor);
		
		inactiveParkingSensor = new ParkingSensors();
		inactiveSensor = new Sensors(size + 1001L, "sensor 110110110", "2,2V", "2", "ParkingArea", false, null);
		inactiveSensor.setParkingSensors(inactiveParkingSensor);

		marker.setId(size + 1002L);
		marker.setName("sensor 97");
		marker.setAddress("Via sensor 97");
		marker.setLat("45.12564");
		marker.setLng("24.65489");
		marker.setState(false);
		marker.setBattery("3,5V");
		marker.setActive(true);

		size = sensorsServices.getAllSensorsFromDB().size();
	}

	@Test
	public void saveSensorDataTest() {
		sensorsServices.saveSensorData(sensor);
		sensorsServices.saveSensorData(inactiveSensor);
		Assert.assertEquals(size + 2, sensorsServices.getAllSensorsFromDB().size());
	}

	@Test
	public void buildSensorFromMarkerTest() {
		Sensors sensor = sensorsServices.buildSensorFromMarker(marker);
		ParkingSensors parkArea = sensor.getParkingSensors();

		Assert.assertEquals(sensor.getId(), marker.getId());
		Assert.assertEquals(sensor.getName(), marker.getName());
		Assert.assertEquals(parkArea.getAddress(), marker.getAddress());
		Assert.assertEquals(parkArea.getLatitude(), marker.getLat());
		Assert.assertEquals(parkArea.getLongitude(), marker.getLng());
		Assert.assertEquals(parkArea.getValue(), marker.getState());
		Assert.assertEquals(sensor.getBattery(), marker.getBattery());
		Assert.assertEquals(sensor.getCharge(), marker.getBattery().substring(0, 1));
		Assert.assertEquals(sensor.isActive(), marker.isActive());

		marker.setId(null);
		marker.setName(null);
		marker.setBattery(null);

		sensor = sensorsServices.buildSensorFromMarker(marker);

		Assert.assertEquals(null, sensor.getId());
		Assert.assertEquals(null, sensor.getName());
		Assert.assertEquals(null, sensor.getBattery());
		Assert.assertEquals(null, sensor.getCharge());
	}

	@Test
	public void getAllSensorsFromDBTest() {
		List<Sensors> sensors = sensorsServices.getAllSensorsFromDB();
		Assert.assertEquals(size, sensors.size());
	}

	@Test
	public void getSensorStateTest() {
		sensorsServices.saveSensorData(sensor);
		sensorsServices.saveSensorData(inactiveSensor);

		boolean state = sensorsServices.getSensorState(sensor.getId());
		Assert.assertEquals(true, state);

		state = sensorsServices.getSensorState(inactiveSensor.getId());
		Assert.assertEquals(false, state);
	}

	@Test
	public void getSensorsByNameTest() {
		sensor.setName("TestSensor");
		inactiveSensor.setName("TestSensor");
		sensorsServices.saveSensorData(sensor);
		sensorsServices.saveSensorData(inactiveSensor);
		List<Sensors> sensors = sensorsServices.getSensorsByName("TestSensor");
		Assert.assertEquals(2, sensors.size());
	}

	@Test
	public void getSensorByIdTest() {
		sensorsServices.saveSensorData(sensor);
		Sensors result = sensorsServices.getSensorById(sensor.getId());
		Assert.assertEquals(sensor.getId(), result.getId());
		Assert.assertEquals(sensor.getName(), result.getName());
		Assert.assertEquals(sensor.getBattery(), result.getBattery());
		Assert.assertEquals(sensor.getCharge(), result.getCharge());
		Assert.assertEquals(sensor.getType(), result.getType());
		Assert.assertEquals(sensor.isActive(), result.isActive());
	}

	@Test
	public void getSensorsByTypeTest() {
		sensor.setType("SensorType");
		inactiveSensor.setType("SensorType");
		sensorsServices.saveSensorData(sensor);
		sensorsServices.saveSensorData(inactiveSensor);
		List<Sensors> result = sensorsServices.getSensorsByType("SensorType");
		Assert.assertEquals(2, result.size());
	}

	@Test
	public void getSensorByNameStartingWithTest() {
		sensor.setName("TestSensor");
		inactiveSensor.setName("TestSensor");
		sensorsServices.saveSensorData(sensor);
		sensorsServices.saveSensorData(inactiveSensor);
		List<Sensors> result = sensorsServices.getSensorByNameStartingWith("Test");
		Assert.assertEquals(2, result.size());
	}

	@Test
	public void getSensorByNameContainingTest() {
		sensor.setName("TestNameContaining");
		inactiveSensor.setName("TestNameContaining");
		sensorsServices.saveSensorData(sensor);
		sensorsServices.saveSensorData(inactiveSensor);
		List<Sensors> result = sensorsServices.getSensorByNameContaining("stNameCo");
		Assert.assertEquals(2, result.size());
	}

	@Test
	public void getSensorByNameEndingWithTest() {
		sensor.setName("TestSensorEndingWith");
		inactiveSensor.setName("TestSensorEndingWith");
		sensorsServices.saveSensorData(sensor);
		sensorsServices.saveSensorData(inactiveSensor);
		List<Sensors> result = sensorsServices.getSensorByNameEndingWith("EndingWith");
		Assert.assertEquals(2, result.size());
	}

	@Test
	public void getSensorByIsActiveTrueTest() {
		sensorsServices.deleteAllSensors();
		sensorsServices.saveSensorData(sensor);
		sensorsServices.saveSensorData(inactiveSensor);
		List<Sensors> result = sensorsServices.getSensorByIsActiveTrue();
		Assert.assertEquals(1, result.size());
	}

	@Test
	public void getSensorByIsActiveFalseTest() {
		sensorsServices.deleteAllSensors();
		sensorsServices.saveSensorData(sensor);
		sensorsServices.saveSensorData(inactiveSensor);
		List<Sensors> result = sensorsServices.getSensorByIsActiveFalse();
		Assert.assertEquals(1, result.size());
	}

	@Test
	public void getLowBatterySensorsTest() {
		// Arrange
		List<Marker> sensors = new ArrayList<>();
		// Act
		Marker marker = new Marker(size + 1004L, "TestSensor", "TestAddress", "TestLat", "TestLng", false, "3,0V",
				false);
		Marker one = new Marker(size + 1005L, "TestSensor", "TestAddress", "TestLat", "TestLng", false, "1,9V", false);
		Marker two = new Marker(size + 1006L, "TestSensor", "TestAddress", "TestLat", "TestLng", false, "0,5V", false);
		Marker three = new Marker(size + 1007L, "TestSensor", "TestAddress", "TestLat", "TestLng", false, "3,7V", true);
		Marker four = new Marker(size + 1008L, "TestSensor", "TestAddress", "TestLat", "TestLng", false, "3,5V", true);
		sensors.add(marker);
		sensors.add(one);
		sensors.add(two);
		sensors.add(three);
		sensors.add(four);

		Markers markers = new Markers(sensors);
		MarkerList markerList = new MarkerList(markers);
		// Assert
		Assert.assertEquals(2, sensorsServices.getLowBatterySensors(markerList).size());
	}

	@Test
	public void getCorruptedSensorsTest() {
		// Arrange
		List<Marker> sensors = new ArrayList<>();
		// Act

		Marker marker = new Marker(size + 1009L, "TestSensor", "TestAddress", "TestLat", "TestLng", false, "3,7V",
				false);
		Marker one = new Marker(size + 1010L, "TestSensor", "TestAddress", "TestLat", "TestLng", false, "3,7V", false);
		Marker two = new Marker(size + 1011L, "TestSensor", "TestAddress", "TestLat", "TestLng", false, "3,7V", false);
		Marker three = new Marker(size + 1012L, "TestSensor", "TestAddress", "TestLat", "TestLng", false, "3,7V", true);
		Marker four = new Marker(size + 1013L, "TestSensor", "TestAddress", "TestLat", "TestLng", false, "3,7V", true);
		sensors.add(marker);
		sensors.add(one);
		sensors.add(two);
		sensors.add(three);
		sensors.add(four);

		Markers markers = new Markers(sensors);
		MarkerList markerList = new MarkerList(markers);
		// Assert
		Assert.assertEquals(3, sensorsServices.getCorruptedSensors(markerList).size());
	}

	@Test
	public void deleteSensorByIdTest() {
		saveSensor(sensor);
		saveSensor(inactiveSensor);
		deleteSensorById(sensor.getId());
		Assert.assertEquals(size + 1, sensorsServices.getAllSensorsFromDB().size());
		deleteSensorById(inactiveSensor.getId());
		Assert.assertEquals(size, sensorsServices.getAllSensorsFromDB().size());
	}

	public void saveSensor(Sensors sensor) {
		sensorsServices.saveSensorData(sensor);
	}

	public void deleteSensorById(Long id) {
		sensorsServices.deleteSensorById(id);
	}

	@Test
	public void deleteAllSensorsByIdTest() {
		Assert.assertEquals(size, sensorsServices.getAllSensorsFromDB().size());
		sensorsServices.deleteAllSensors();
		Assert.assertEquals(0, sensorsServices.getAllSensorsFromDB().size());
	}
}