package it.synclab.smartparking;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import it.synclab.smartparking.model.Marker;
import it.synclab.smartparking.repository.model.ParkingArea;
import it.synclab.smartparking.repository.model.Sensor;
import it.synclab.smartparking.service.ParkingAreaServices;
import it.synclab.smartparking.service.SensorServices;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParkingAreaServicesTest {

	@Autowired
	private ParkingAreaServices parkingAreaServices;

	@Autowired
	private SensorServices sensorServices;

	ParkingArea parkingArea = new ParkingArea();
	Sensor sensor;
	Marker marker = new Marker();
	int size = 0;

	@Before
	public void init() {
		parkingArea = new ParkingArea("45.00000", "24.00000", "Sensor address", false, null);

		marker.setId(99999999L);
		marker.setName("sensor 97");
		marker.setAddress("Via sensor 97");
		marker.setLat("45.00000");
		marker.setLng("24.00000");
		marker.setState(false);
		marker.setBattery("3,5V");
		marker.setActive(true);

		sensor = sensorServices.buildSensorFromMarker(marker);

		size = parkingAreaServices.getAllParkingAreas().size();

	}

	@Transactional
	@Test
	public void getAllParkingAreasTest(){
		sensorServices.saveSensorData(sensor);
		List<ParkingArea> parkingAreas = parkingAreaServices.getAllParkingAreas();
		Assert.assertTrue(parkingAreas.size() > 0);
	}

	@Test
	public void buildPArkingAreaFromMarkerTest() {

		ParkingArea parkArea = parkingAreaServices.buildParkingAreaFromMarker(marker);

		Assert.assertEquals(parkArea.getAddress(), marker.getAddress());
		Assert.assertEquals(parkArea.getLatitude(), marker.getLat());
		Assert.assertEquals(parkArea.getLongitude(), marker.getLng());
		Assert.assertEquals(parkArea.getValue(), marker.getState());

		marker.setId(null);
		marker.setAddress(null);
		marker.setLat(null);
		marker.setLng(null);

		parkArea = parkingAreaServices.buildParkingAreaFromMarker(marker);

		Assert.assertEquals(null, parkingArea.getId());
	}

	@Test
	@Transactional
	public void getParkingAreaByIdTest() {
		sensorServices.saveSensorData(sensor);
		Sensor fromDB = sensorServices.getSensorById(sensor.getId());
		Long id = fromDB.getParkingArea().get(0).getId();
		ParkingArea result = parkingAreaServices.getParkingAreaById(id);
		Assert.assertEquals(id, result.getId());
	}

	@Transactional
	@Test
	public void getParkingAreaBySensorIdTest() {
		sensorServices.saveSensorData(sensor);
		Sensor fromDB = sensorServices.getSensorById(sensor.getId());
		Long sensorId = fromDB.getId();
		ParkingArea result = parkingAreaServices.getParkingAreaBySensorId(sensorId);
		Assert.assertEquals(sensorId, result.getFkSensorId());
	}

	@Test
	@Transactional
	public void getParkingAreaLatitudeByIdTest() {
		sensorServices.saveSensorData(sensor);
		Sensor fromDB = sensorServices.getSensorById(sensor.getId());
		Long id = fromDB.getParkingArea().get(0).getId();
		String latitude = parkingAreaServices.getParkingAreaLatitudeById(id);
		Assert.assertEquals(latitude, sensor.getParkingArea().get(0).getLatitude());
	}

	@Test
	@Transactional
	public void getParkingAreaLongitudeByIdTest() {
		sensorServices.saveSensorData(sensor);
		Sensor fromDB = sensorServices.getSensorById(sensor.getId());
		Long id = fromDB.getParkingArea().get(0).getId();
		String longitude = parkingAreaServices.getParkingAreaLongitudeById(id);
		Assert.assertEquals(longitude, sensor.getParkingArea().get(0).getLongitude());
	}

	@Test
	@Transactional
	public void getParkingAreaAddressByIdTest() {
		sensorServices.saveSensorData(sensor);
		Sensor fromDB = sensorServices.getSensorById(sensor.getId());
		Long id = fromDB.getParkingArea().get(0).getId();
		String address = parkingAreaServices.getParkingAreaAddressById(id);
		Assert.assertEquals(address, sensor.getParkingArea().get(0).getAddress());
	}

	@Test
	@Transactional
	public void getParkingAreaStateByIdTest() {
		sensorServices.saveSensorData(sensor);
		Sensor fromDB = sensorServices.getSensorById(sensor.getId());
		Long id = fromDB.getParkingArea().get(0).getId();
		boolean state = parkingAreaServices.getParkingAreaStateById(id);
		Assert.assertEquals(state, sensor.getParkingArea().get(0).getValue());
	}

	@Test
	@Transactional
	public void getFreeParkingAreaTest() {
		sensorServices.deleteAllSensors();
		parkingAreaServices.deleteAllParkingArea();
		List<ParkingArea> parkArea = new ArrayList<>();
		parkArea.add(parkingArea);
		for (int i = 0; i < 4; i++){
		Sensor sensor = new Sensor(i + 1L, "Sensor " + i, "3,7V", "3", "ParkingArea", true, parkArea); 
		sensorServices.saveSensorData(sensor);
		}
		parkingArea.setValue(true);
		for (int i = 0; i < 5; i++){
			Sensor sensor = new Sensor(i + 5L, "Sensor " + i, "3,7V", "3", "ParkingArea", true, parkArea); 
			sensorServices.saveSensorData(sensor);
			}
		List<ParkingArea> result = parkingAreaServices.getFreeParkingArea();
		Assert.assertEquals(4, result.size());
	}

	@Test
	@Transactional
	public void getOccupyParkingAreaTest() {
		sensorServices.deleteAllSensors();
		parkingAreaServices.deleteAllParkingArea();
		List<ParkingArea> parkArea = new ArrayList<>();
		parkArea.add(parkingArea);
		for (int i = 0; i < 4; i++){
		Sensor sensor = new Sensor(i + 1L, "Sensor " + i, "3,7V", "3", "ParkingArea", true, parkArea); 
		sensorServices.saveSensorData(sensor);
		}
		parkingArea.setValue(true);
		for (int i = 0; i < 5; i++){
			Sensor sensor = new Sensor(i + 5L, "Sensor " + i, "3,7V", "3", "ParkingArea", true, parkArea); 
			sensorServices.saveSensorData(sensor);
			}
		List<ParkingArea> result = parkingAreaServices.getOccupyParkingArea();
		Assert.assertEquals(5, result.size());
	}

	@Test
	@Transactional
	public void getParkingAreaByLatitudeAndLongitudeTest() {
		sensor.getParkingArea().get(0).setLatitude("TestLatitude");
		sensor.getParkingArea().get(0).setLongitude("TestLongitude");
		sensorServices.saveSensorData(sensor);
		String latitude = sensor.getParkingArea().get(0).getLatitude();
		String longitude = sensor.getParkingArea().get(0).getLongitude();
		ParkingArea fromDB = parkingAreaServices.getParkingAreaByLatitudeAndLongitude(latitude,longitude);
		Assert.assertEquals(latitude, fromDB.getLatitude());
		Assert.assertEquals(longitude, fromDB.getLongitude());
	}

	@Test
	@Transactional
	public void getParkingAreaLastUpdateDateBySensorIdTest() {
		sensor.getParkingArea().get(0).setLastUpdate(LocalDateTime.of(1997, 11, 30, 0, 0));
		sensorServices.saveSensorData(sensor);
		LocalDateTime lastUpdate = sensor.getParkingArea().get(0).getLastUpdate();
		LocalDateTime result = parkingAreaServices.getParkingAreaLastUpdateDateBySensorId(sensor.getId());
		Assert.assertEquals(lastUpdate, result);
	}

	@Test
	@Transactional
	public void deleteParkingAreaBySensorIdTest() {
		sensorServices.saveSensorData(sensor);
		int size = parkingAreaServices.getAllParkingAreas().size();
		Long sensorId = sensor.getId();
		Sensor fromDB = sensorServices.getSensorById(sensorId);
		parkingAreaServices.deleteParkingAreaBySensorId(sensorId);
		Assert.assertEquals(size-1, parkingAreaServices.getAllParkingAreas().size());
	}

	@Test
	@Transactional
	public void deleteParkingAreaByIdTest() {
		int size = parkingAreaServices.getAllParkingAreas().size();
		sensorServices.saveSensorData(sensor);
		Assert.assertEquals(size + 1, parkingAreaServices.getAllParkingAreas().size());
		Long id = sensorServices.getSensorById(sensor.getId()).getParkingArea().get(0).getId();
		parkingAreaServices.deleteParkingAreaById(id);
		Assert.assertEquals(size, parkingAreaServices.getAllParkingAreas().size());
	}
	
	@Test
	@Transactional
	public void deleteAllParkingAreasTest() {
		int size = parkingAreaServices.getAllParkingAreas().size();
		if (size > 0) {
			parkingAreaServices.deleteAllParkingArea();
			size = parkingAreaServices.getAllParkingAreas().size();
		}
		Assert.assertEquals(0, size);
	}

}