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
import it.synclab.smartparking.repository.model.Sensor;
import it.synclab.smartparking.repository.model.SensorsMaintainer;
import it.synclab.smartparking.service.SensorMaintainerServices;
import it.synclab.smartparking.service.SensorServices;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SensorMaintainerServicesTest {

    @Autowired
    private SensorMaintainerServices sensorMaintainerServices;

    @Autowired
    private SensorServices sensorServices;

    Marker marker = new Marker();
    Sensor sensor = new Sensor();
    SensorsMaintainer maintainer = new SensorsMaintainer();
    SensorsMaintainer maintainer2 = new SensorsMaintainer();
    List<SensorsMaintainer> list = new ArrayList<>();

    int size = 0;

    @Before
    public void init() {

        marker.setId(1234567890123456789L);
        marker.setName("TestSensor");
        marker.setAddress("TestAddress");
        marker.setLat("97.00000");
        marker.setLng("97.00000");
        marker.setState(false);
        marker.setBattery("3,7V");
        marker.setActive(true);

        maintainer = sensorMaintainerServices.buildSensorsMaintainerFromMarker(marker);
        list.add(maintainer);

        sensor = sensorServices.buildSensorFromMarker(marker);
        sensor.setMaintainers(list);
        size = sensorMaintainerServices.getAllSensorsMaintainerData().size();
    }

    @Test
    public void buildSensorsMaintainerFromMarkerTest() {
        SensorsMaintainer sensorsMaintainer = sensorMaintainerServices.buildSensorsMaintainerFromMarker(marker);

        Assert.assertEquals(marker.getId(), sensorsMaintainer.getFkSensorId());
        Assert.assertEquals("ParkingArea", sensorsMaintainer.getType());
        Assert.assertNull(sensorsMaintainer.getId());
        Assert.assertNull(sensorsMaintainer.getOwnerName());
        Assert.assertNull(sensorsMaintainer.getOwnerSurname());
        Assert.assertNull(sensorsMaintainer.getCompany());
        Assert.assertNull(sensorsMaintainer.getPhoneNumber());
        Assert.assertNull(sensorsMaintainer.getMail());
        Assert.assertFalse(sensorsMaintainer.isToBeCharged());
        Assert.assertFalse(sensorsMaintainer.isToBeRepaired());

        marker.setId(null);
        sensorsMaintainer = sensorMaintainerServices.buildSensorsMaintainerFromMarker(marker);
        Assert.assertNull(sensorsMaintainer.getFkSensorId());
    }

    @Test
    public void saveSensorsMaintainerDataTest() {
        maintainer2.setId(1234567890123456789L);
        maintainer2.setFkSensorId(10L);
        maintainer2.setType("ParkingArea");
        maintainer2.setOwnerName("TestOwnerName");
        maintainer2.setOwnerSurname("TestOwnerSurname");
        maintainer2.setCompany("TestCompany");
        maintainer2.setPhoneNumber("TestPhoneNumber");
        maintainer2.setMail("TestMail");
        maintainer2.setToBeCharged(false);
        maintainer2.setToBeRepaired(false);
        sensorMaintainerServices.saveSensorsMaintainerData(maintainer2);
        sensorMaintainerServices.deleteSensorMaintainersById(maintainer2.getId());
        Assert.assertEquals(size, sensorMaintainerServices.getAllSensorsMaintainerData().size());
    }

    @Transactional
    @Test
    public void getAllSensorsMaintainerDataTest() {
        sensorServices.saveSensorData(sensor);
        int result = sensorMaintainerServices.getAllSensorsMaintainerData().size();
        Assert.assertEquals(size + 1, result);
    }

    @Transactional
    @Test
    public void getSensorsMaintainerDataBySensorIdTest() {
        sensorServices.saveSensorData(sensor);
        Long result = sensorMaintainerServices.getSensorsMaintainerDataBySensorId(sensor.getId()).get(0).getFkSensorId();
        Assert.assertEquals(sensor.getId(), result);
    }

    @Transactional
    @Test
    public void getSensorsMaintainerDataByIdTest() {
        sensorServices.saveSensorData(sensor);
        Long maintainerId = sensorServices.getSensorById(sensor.getId()).getMaintainer().get(0).getId();
        SensorsMaintainer result = sensorMaintainerServices.getSensorsMaintainerDataById(maintainerId);
        Assert.assertEquals(maintainer.getFkSensorId(), result.getFkSensorId());
        Assert.assertEquals(maintainer.getType(), result.getType());
        Assert.assertNull(result.getOwnerName());
    }

    @Transactional
    @Test
    public void deleteSensorMaintainersBySensorIdTest() {
        sensorServices.saveSensorData(sensor);
        Assert.assertNotEquals(size, sensorMaintainerServices.getAllSensorsMaintainerData().size());
        sensorMaintainerServices.deleteSensorMaintainersBySensorId(sensor.getId());
        Assert.assertEquals(size, sensorMaintainerServices.getAllSensorsMaintainerData().size());
    }

    @Transactional
    @Test
    public void deleteSensorMaintainerByIdTest() {
        sensorServices.saveSensorData(sensor);
        Assert.assertNotEquals(size, sensorMaintainerServices.getAllSensorsMaintainerData().size());
        sensorMaintainerServices.deleteSensorMaintainersById(sensorServices.getSensorById(sensor.getId()).getMaintainer().get(0).getId());
        Assert.assertEquals(size, sensorMaintainerServices.getAllSensorsMaintainerData().size());
    }

    @Transactional
    @Test
    public void deleteAllSensorMaintainers() {
        if(size > 0) {
            sensorMaintainerServices.deleteAllSensorMaintainers();
            Assert.assertEquals(0, sensorMaintainerServices.getAllSensorsMaintainerData().size());
        }
    }
}
