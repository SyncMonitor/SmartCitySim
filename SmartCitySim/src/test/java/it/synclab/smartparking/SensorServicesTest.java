package it.synclab.smartparking;

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
import it.synclab.smartparking.repository.model.ParkingArea;
import it.synclab.smartparking.repository.model.Sensor;
import it.synclab.smartparking.service.SensorServices;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class SensorServicesTest {

	@Autowired
	private SensorServices sensorServices;

	Sensor sensor = null;
	Sensor inactiveSensor = null;
	List<ParkingArea> parkingArea = new ArrayList<>();
	List<ParkingArea> InactiveparkingArea = new ArrayList<>();
	Marker marker = new Marker();
	int size = 0;


	@Before
	public void init() {
		parkingArea.add(new ParkingArea("45.12564", "24.65489", "Via Indirizzo di prova 100", true, null));
		sensor = new Sensor(size + 1000L, "sensor 109109109", "3,7V", "3", "ParkingArea", true, null);
		sensor.setParkingArea(parkingArea);

		inactiveSensor = new Sensor(size + 1001L, "sensor 110110110", "2,2V", "2", "ParkingArea", false, null);
		inactiveSensor.setParkingArea(InactiveparkingArea);

		marker.setId(size + 1002L);
		marker.setName("sensor 97");
		marker.setAddress("Via sensor 97");
		marker.setLat("45.12564");
		marker.setLng("24.65489");
		marker.setState(false);
		marker.setBattery("3,5V");
		marker.setActive(true);

		size = sensorServices.getAllSensorsFromDB().size();
	}

	@Test
	public void saveSensorDataTest() {
		sensorServices.saveSensorData(sensor);
		sensorServices.saveSensorData(inactiveSensor);
		Assert.assertEquals(size + 2, sensorServices.getAllSensorsFromDB().size());
	}

	@Test
	public void buildSensorFromMarkerTest() {
		Sensor sensor = sensorServices.buildSensorFromMarker(marker);
		ParkingArea parkArea = sensor.getParkingArea().get(0);

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

		sensor = sensorServices.buildSensorFromMarker(marker);

		Assert.assertEquals(sensor.getId(), null);
		Assert.assertEquals(sensor.getName(), null);
		Assert.assertEquals(sensor.getBattery(), null);
		Assert.assertEquals(sensor.getCharge(), null);
	}

	@Test
	public void getAllSensorsFromDBTest() {
		List<Sensor> sensors = sensorServices.getAllSensorsFromDB();
		Assert.assertEquals(size, sensors.size());
	}

	@Test
	public void getSensorStateTest() {
		sensorServices.saveSensorData(sensor);
		sensorServices.saveSensorData(inactiveSensor);

		boolean state = sensorServices.getSensorState(sensor.getId());
		Assert.assertEquals(true, state);

		state = sensorServices.getSensorState(inactiveSensor.getId());
		Assert.assertEquals(false, state);
	}

	@Test
	public void getSensorsByNameTest() {
		sensor.setName("TestSensor");
		inactiveSensor.setName("TestSensor");
		sensorServices.saveSensorData(sensor);
		sensorServices.saveSensorData(inactiveSensor);
		List<Sensor> sensors = sensorServices.getSensorsByName("TestSensor");
		Assert.assertEquals(2, sensors.size());
	}

	@Test
	public void getSensorByIdTest() {
		sensorServices.saveSensorData(sensor);
		Sensor result = sensorServices.getSensorById(sensor.getId());
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
		sensorServices.saveSensorData(sensor);
		sensorServices.saveSensorData(inactiveSensor);
		List<Sensor> result = sensorServices.getSensorsByType("SensorType");
		Assert.assertEquals(2, result.size());
	}

	@Test
	public void getSensorByNameStartingWithTest() {
		sensor.setName("TestSensor");
		inactiveSensor.setName("TestSensor");
		sensorServices.saveSensorData(sensor);
		sensorServices.saveSensorData(inactiveSensor);
		List<Sensor> result = sensorServices.getSensorByNameStartingWith("Test");
		Assert.assertEquals(2, result.size());
	}

	@Test
	public void getSensorByNameContainingTest() {
		sensor.setName("TestNameContaining");
		inactiveSensor.setName("TestNameContaining");
		sensorServices.saveSensorData(sensor);
		sensorServices.saveSensorData(inactiveSensor);
		List<Sensor> result = sensorServices.getSensorByNameContaining("stNameCo");
		Assert.assertEquals(2, result.size());
	}

	@Test
	public void getSensorByNameEndingWithTest() {
		sensor.setName("TestSensorEndingWith");
		inactiveSensor.setName("TestSensorEndingWith");
		sensorServices.saveSensorData(sensor);
		sensorServices.saveSensorData(inactiveSensor);
		List<Sensor> result = sensorServices.getSensorByNameEndingWith("EndingWith");
		Assert.assertEquals(2, result.size());
	}

	@Test
	public void getSensorByIsActiveTrueTest() {
		sensorServices.deleteAllSensors();
		sensorServices.saveSensorData(sensor);
		sensorServices.saveSensorData(inactiveSensor);
		List<Sensor> result = sensorServices.getSensorByIsActiveTrue();
		Assert.assertEquals(1, result.size());
	}

	@Test
	public void getSensorByIsActiveFalseTest() {
		sensorServices.deleteAllSensors();
		sensorServices.saveSensorData(sensor);
		sensorServices.saveSensorData(inactiveSensor);
		List<Sensor> result = sensorServices.getSensorByIsActiveFalse();
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
		Assert.assertEquals(2, sensorServices.getLowBatterySensors(markerList).size());
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
		Assert.assertEquals(3, sensorServices.getCorruptedSensors(markerList).size());
	}

	@Test
	public void deleteSensorByIdTest() {
		saveSensor(sensor);
		saveSensor(inactiveSensor);
		deleteSensorById(sensor.getId());
		Assert.assertEquals(size + 1, sensorServices.getAllSensorsFromDB().size());
		deleteSensorById(inactiveSensor.getId());
		Assert.assertEquals(size, sensorServices.getAllSensorsFromDB().size());
	}

	public void saveSensor(Sensor sensor) {
		sensorServices.saveSensorData(sensor);
	}

	public void deleteSensorById(Long id) {
		sensorServices.deleteSensorById(id);
	}

	@Test
	public void deleteAllSensorsByIdTest() {
		Assert.assertEquals(size, sensorServices.getAllSensorsFromDB().size());
		sensorServices.deleteAllSensors();
		Assert.assertEquals(0, sensorServices.getAllSensorsFromDB().size());
	}
}