package it.synclab.smartparking.service;

import java.time.LocalDateTime;
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
import it.synclab.smartparking.repository.model.ParkingAreaStats;
import it.synclab.smartparking.repository.model.Sensors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParkingStatsServicesTest {

    @Autowired
    private SensorsServices sensorsServices;

    @Autowired
    private ParkingSensorsServices parkingAreaServices;

    @Autowired
    private ParkingStatsServices parkingStatsServices;

    Marker marker;
    Sensors sensor;
    ParkingAreaStats stat;
    ParkingAreaStats stat2 = new ParkingAreaStats();
    ParkingSensors parkArea;
    int size = 0;
    Long id = 0L;

    @Before
    public void init() {
        id = sensorsServices.getAllSensorsFromDB().get(0).getId();

        marker = new Marker();

        marker.setId(1234567890123456789L);
        marker.setName("TestSensor");
        marker.setAddress("TestAddress");
        marker.setLat("97.00000");
        marker.setLng("97.00000");
        marker.setState(false);
        marker.setBattery("3,7V");
        marker.setActive(true);

        stat2.setId(1234567890123456788L);
        stat2.setFkSensorId(id);
        stat2.setLastUpdate(LocalDateTime.of(1980, 1, 1, 0, 0, 0));
        stat2.setValue(true);

        sensor = sensorsServices.buildSensorFromMarker(marker);

        size = parkingStatsServices.getParkingAreaStats().size();
    }

    @Transactional
    @Test
    public void buildParkingAreaStatsFromParkingAreaTest() {
    	sensorsServices.saveSensorData(sensor);
        ParkingSensors parkingArea = parkingAreaServices.getParkingSensorBySensorId(1234567890123456789L);
        parkingArea.setValue(!parkingArea.getValue());
        stat = parkingStatsServices.buildParkingAreaStatsFromParkingArea(parkingArea);
        Assert.assertNotNull(stat);
        Assert.assertEquals(parkingArea.getFkSensorId(), stat.getFkSensorId());
        Assert.assertEquals(parkingArea.getValue(), stat.isValue());
        Assert.assertEquals(parkingArea.getTimestamp(), stat.getLastUpdate());

        parkingArea.setSensors(null);
        parkingArea.setTimestamp(null);
        stat = parkingStatsServices.buildParkingAreaStatsFromParkingArea(parkingArea);
        Assert.assertNotNull(stat);
    }

    @Transactional
    @Test
    public void saveSensorsMaintainerDataTest() {
        parkingStatsServices.saveParkingAreaStats(stat2);
        Assert.assertEquals(size + 1, parkingStatsServices.getParkingAreaStats().size());
    }

    @Transactional
    @Test
    public void getParkingAreaStatsTest() {
        parkingStatsServices.saveParkingAreaStats(stat2);
    }

    @Transactional
    @Test
    public void getParkingAreaStatsById() {
        stat2.setFkSensorId(id);
        parkingStatsServices.saveParkingAreaStats(stat2);
        ParkingAreaStats result = parkingStatsServices
                .getParkingAreaStatsById(parkingStatsServices.getParkingAreaStats().get(0).getId());
        Assert.assertEquals(stat2.getLastUpdate(), result.getLastUpdate());
        Assert.assertEquals(stat2.isValue(), result.isValue());
    }

    @Transactional
    @Test
    public void getParkingAreaStatsBySensorIdTest() {
        int size = parkingStatsServices.getParkingAreaStatsBySensorId(id).size();
        stat2.setFkSensorId(id);
        parkingStatsServices.saveParkingAreaStats(stat2);
        Assert.assertEquals(size + 1, parkingStatsServices.getParkingAreaStatsBySensorId(id).size());
    }

    @Transactional
    @Test
    public void getParkingAreaStatsFromDateTest() {
        stat2.setFkSensorId(id);
        parkingStatsServices.saveParkingAreaStats(stat2);
        List<ParkingAreaStats> result = parkingStatsServices.getParkingAreaStatsFromDate(LocalDateTime.of(1980, 1, 1, 0, 0, 0));
        Assert.assertEquals(size + 1, result.size());
    }

    @Transactional
    @Test
    public void getParkingAreaStatsFromDateToDateTest() {
        stat2.setFkSensorId(id);
        parkingStatsServices.saveParkingAreaStats(stat2);
        stat2.setFkSensorId(8L);
        parkingStatsServices.saveParkingAreaStats(stat2);
        List<ParkingAreaStats> result = parkingStatsServices.getParkingAreaStatsFromDateToDate(LocalDateTime.of(1979, 1, 1, 0, 0, 0),
                LocalDateTime.of(1980, 1, 1, 1, 0, 0));
        Assert.assertEquals(2, result.size());
    }

    @Transactional
    @Test
    public void getParkingAreaStatsBySensorIdFromDateTest() {
        int size = parkingStatsServices.getParkingAreaStatsBySensorIdFromDate(id, LocalDateTime.of(1980, 1, 1, 0, 0, 0)).size();
        stat2.setFkSensorId(id);
        parkingStatsServices.saveParkingAreaStats(stat2);
        List<ParkingAreaStats> result = parkingStatsServices.getParkingAreaStatsBySensorIdFromDate(id, LocalDateTime.of(1979, 1, 1, 0, 0, 0));
        Assert.assertEquals(size + 1, result.size());
    }

    @Transactional
    @Test
    public void getParkingAreaStatsBySensorIdFromDateToDateTest() {
        int size = parkingStatsServices.getParkingAreaStatsBySensorIdFromDateToDate(id, LocalDateTime.of(1979, 1, 1, 0, 0, 0),
                LocalDateTime.now()).size();
        stat2.setFkSensorId(id);
        parkingStatsServices.saveParkingAreaStats(stat2);
        stat2.setLastUpdate(LocalDateTime.of(1997, 11, 30, 11, 07, 36));
        parkingStatsServices.saveParkingAreaStats(stat2);
        List<ParkingAreaStats> result = parkingStatsServices.getParkingAreaStatsBySensorIdFromDateToDate(id, LocalDateTime.of(1979, 1, 1, 0, 0, 0),
                LocalDateTime.now());
        Assert.assertEquals(size + 2, result.size());
    }

    @Transactional
    @Test
    public void deleteParkingAreaStatsBeforeDateTest() {
        parkingStatsServices.saveParkingAreaStats(stat2);
        stat2.setFkSensorId(8L);
        parkingStatsServices.saveParkingAreaStats(stat2);
        int size = parkingStatsServices.getParkingAreaStatsFromDateToDate(LocalDateTime.of(1979,1,1,0,0,0), LocalDateTime.of(1995,1,1,0,0,0)).size();
        Assert.assertTrue(size > 0);
        parkingStatsServices.deleteParkingAreaStatsBeforeDate(LocalDateTime.of(1995, 12, 31, 23, 59, 59));
        size = parkingStatsServices.getParkingAreaStatsFromDateToDate(LocalDateTime.of(1979,1,1,0,0,0), LocalDateTime.of(1995,1,1,0,0,0)).size();
        Assert.assertTrue("fail Delete before date",size == 0);
    }

    @Transactional
    @Test
    public void deleteParkingAreaStatsById() {
        parkingStatsServices.saveParkingAreaStats(stat2);
        Long parkingAreaId = parkingStatsServices.getParkingAreaStatsBySensorId(id).get(0).getId();
        Assert.assertNotNull(parkingStatsServices.getParkingAreaStatsById(parkingAreaId));
        parkingStatsServices.deleteParkingAreaStatsById(parkingAreaId);
        Assert.assertNull(parkingStatsServices.getParkingAreaStatsById(parkingAreaId));
    }

    @Transactional
    @Test
    public void deleteParkingAreaStatsBySensorId() {
        parkingStatsServices.saveParkingAreaStats(stat2);
        stat2.setFkSensorId(8L);
        parkingStatsServices.saveParkingAreaStats(stat2);
        int size = parkingStatsServices.getParkingAreaStatsBySensorIdFromDateToDate(id, LocalDateTime.of(1979,1,1,0,0,0), LocalDateTime.of(1995,1,1,0,0,0)).size();
        Assert.assertTrue(size > 0);
        parkingStatsServices.deleteParkingAreaStatsBySensorId(id);
        size = parkingStatsServices.getParkingAreaStatsBySensorId(id).size();
        Assert.assertTrue("fail Delete by sensorId", size == 0);
    }

    @Transactional
    @Test
    public void deleteAllParkingStatsTest() {
        parkingStatsServices.saveParkingAreaStats(stat2);
        stat2.setFkSensorId(8L);
        parkingStatsServices.saveParkingAreaStats(stat2);
        int size = parkingStatsServices.getParkingAreaStats().size();
        Assert.assertTrue(size > 0);
        parkingStatsServices.deleteAllParkingStats();
        size = parkingStatsServices.getParkingAreaStats().size();
        Assert.assertTrue("fail Delete all", size == 0);
    }

}
