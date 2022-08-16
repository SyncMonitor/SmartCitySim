package it.synclab.smartparking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import it.synclab.smartparking.model.Marker;
import it.synclab.smartparking.repository.model.ParkingArea;
import it.synclab.smartparking.repository.model.ParkingAreaStats;
import it.synclab.smartparking.repository.model.Sensor;
import it.synclab.smartparking.service.ParkingAreaServices;
import it.synclab.smartparking.service.ParkingStatsServices;
import it.synclab.smartparking.service.SensorServices;
import org.junit.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParkingStatsServicesTest {

    @Autowired
    private SensorServices sensorServices;

    @Autowired
    private ParkingAreaServices parkingAreaServices;

    @Autowired
    private ParkingStatsServices parkingStatsServices;

    Marker marker;
    Sensor sensor;
    ParkingAreaStats stat;
    ParkingAreaStats stat2 = new ParkingAreaStats();
    ParkingArea parkArea;
    int size;

    @Before
    public void init() {
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
        stat2.setFkSensorId(10L);
        stat2.setLastUpdate(LocalDateTime.of(1980, 1, 1, 0, 0, 0));
        stat2.setValue(true);

        sensor = sensorServices.buildSensorFromMarker(marker);

        size = parkingStatsServices.getParkingAreaStats().size();
    }

    @Transactional
    @Test
    public void buildParkingAreaStatsFromParkingAreaTest() {
        sensorServices.saveSensorData(sensor);
        ParkingArea parkingArea = parkingAreaServices.getParkingAreaBySensorId(1234567890123456789L);
        parkingArea.setValue(!parkingArea.getValue());
        stat = parkingStatsServices.buildParkingAreaStatsFromParkingArea(parkingArea);
        Assert.assertNotNull(stat);
        Assert.assertEquals(parkingArea.getFkSensorId(), stat.getFkSensorId());
        Assert.assertEquals(parkingArea.getValue(), stat.isValue());
        Assert.assertEquals(parkingArea.getLastUpdate(), stat.getLastUpdate());

        parkingArea.setSensorId(null);
        parkingArea.setLastUpdate(null);
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
        stat2.setFkSensorId(10L);
        parkingStatsServices.saveParkingAreaStats(stat2);
        ParkingAreaStats result = parkingStatsServices
                .getParkingAreaStatsById(parkingStatsServices.getParkingAreaStats().get(0).getId());
        Assert.assertEquals(stat2.getLastUpdate(), result.getLastUpdate());
        Assert.assertEquals(stat2.isValue(), result.isValue());
    }

    @Transactional
    @Test
    public void getParkingAreaStatsBySensorIdTest() {
        int size = parkingStatsServices.getParkingAreaStatsBySensorId(10L).size();
        stat2.setFkSensorId(10L);
        parkingStatsServices.saveParkingAreaStats(stat2);
        Assert.assertEquals(size + 1, parkingStatsServices.getParkingAreaStatsBySensorId(10L).size());
    }

    @Transactional
    @Test
    public void getParkingAreaStatsFromDateTest() {
        stat2.setFkSensorId(10L);
        parkingStatsServices.saveParkingAreaStats(stat2);
        List<ParkingAreaStats> result = parkingStatsServices.getParkingAreaStatsFromDate(LocalDateTime.of(1980, 1, 1, 0, 0, 0));
        Assert.assertEquals(size + 1, result.size());
    }

    @Transactional
    @Test
    public void getParkingAreaStatsFromDateToDateTest() {
        stat2.setFkSensorId(10L);
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
        int size = parkingStatsServices.getParkingAreaStatsBySensorIdFromDate(10L, LocalDateTime.of(1980, 1, 1, 0, 0, 0)).size();
        stat2.setFkSensorId(10L);
        parkingStatsServices.saveParkingAreaStats(stat2);
        List<ParkingAreaStats> result = parkingStatsServices.getParkingAreaStatsBySensorIdFromDate(10L, LocalDateTime.of(1979, 1, 1, 0, 0, 0));
        Assert.assertEquals(size + 1, result.size());
    }

    @Transactional
    @Test
    public void getParkingAreaStatsBySensorIdFromDateToDateTest() {
        int size = parkingStatsServices.getParkingAreaStatsBySensorIdFromDateToDate(10L, LocalDateTime.of(1979, 1, 1, 0, 0, 0),
                LocalDateTime.now()).size();
        stat2.setFkSensorId(10L);
        parkingStatsServices.saveParkingAreaStats(stat2);
        stat2.setLastUpdate(LocalDateTime.of(1997, 11, 30, 11, 07, 36));
        parkingStatsServices.saveParkingAreaStats(stat2);
        List<ParkingAreaStats> result = parkingStatsServices.getParkingAreaStatsBySensorIdFromDateToDate(10L, LocalDateTime.of(1979, 1, 1, 0, 0, 0),
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
        Long id = parkingStatsServices.getParkingAreaStatsBySensorId(10L).get(0).getId();
        Assert.assertNotNull(parkingStatsServices.getParkingAreaStatsById(id));
        parkingStatsServices.deleteParkingAreaStatsById(id);
        Assert.assertNull(parkingStatsServices.getParkingAreaStatsById(id));
    }

    @Transactional
    @Test
    public void deleteParkingAreaStatsBySensorId() {
        parkingStatsServices.saveParkingAreaStats(stat2);
        stat2.setFkSensorId(8L);
        parkingStatsServices.saveParkingAreaStats(stat2);
        int size = parkingStatsServices.getParkingAreaStatsBySensorIdFromDateToDate(10L, LocalDateTime.of(1979,1,1,0,0,0), LocalDateTime.of(1995,1,1,0,0,0)).size();
        Assert.assertTrue(size > 0);
        parkingStatsServices.deleteParkingAreaStatsBySensorId(10L);
        size = parkingStatsServices.getParkingAreaStatsBySensorId(10L).size();
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
