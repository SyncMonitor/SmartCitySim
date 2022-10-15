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

import it.synclab.smartparking.model.Maintainer;
import it.synclab.smartparking.model.Marker;
import it.synclab.smartparking.repository.model.Sensors;
import it.synclab.smartparking.repository.model.SensorsMaintainer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SensorMaintainerServicesTest {

    @Autowired
    private SensorMaintainerServices sensorMaintainerServices;

    @Autowired
    private SensorsServices sensorsServices;

    Marker marker = new Marker();
    Sensors sensor = new Sensors();
    SensorsMaintainer maintainer = new SensorsMaintainer();
    SensorsMaintainer maintainer2 = new SensorsMaintainer();
    List<SensorsMaintainer> list = new ArrayList<>();
    Long id = 0L;

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

        sensor = sensorsServices.buildSensorFromMarker(marker);
        sensor.setMaintainers(list);
        size = sensorMaintainerServices.getAllSensorsMaintainerData().size();
        id = sensorsServices.getAllSensorsFromDB().get(0).getId();
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
        maintainer2.setFkSensorId(id);
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
    	sensorsServices.saveSensorData(sensor);
        int result = sensorMaintainerServices.getAllSensorsMaintainerData().size();
        Assert.assertEquals(size + 1, result);
    }

    @Transactional
    @Test
    public void getSensorsMaintainerDataBySensorIdTest() {
    	sensorsServices.saveSensorData(sensor);
        Long result = sensorMaintainerServices.getSensorsMaintainerDataBySensorId(sensor.getId()).get(0).getFkSensorId();
        Assert.assertEquals(sensor.getId(), result);
    }

    @Transactional
    @Test
    public void getSensorsMaintainerDataByIdTest() {
    	sensorsServices.saveSensorData(sensor);
        Long maintainerId = sensorsServices.getSensorById(sensor.getId()).getMaintainers().get(0).getId();
        SensorsMaintainer result = sensorMaintainerServices.getSensorsMaintainerDataById(maintainerId);
        Assert.assertEquals(maintainer.getFkSensorId(), result.getFkSensorId());
        Assert.assertEquals(maintainer.getType(), result.getType());
        Assert.assertNull(result.getOwnerName());
    }

    @Transactional
    @Test
    public void deleteSensorMaintainersBySensorIdTest() {
    	sensorsServices.saveSensorData(sensor);
        Assert.assertNotEquals(size, sensorMaintainerServices.getAllSensorsMaintainerData().size());
        sensorMaintainerServices.deleteSensorMaintainersBySensorId(sensor.getId());
        Assert.assertEquals(size, sensorMaintainerServices.getAllSensorsMaintainerData().size());
    }

    @Transactional
    @Test
    public void deleteSensorMaintainerByIdTest() {
    	sensorsServices.saveSensorData(sensor);
        Assert.assertNotEquals(size, sensorMaintainerServices.getAllSensorsMaintainerData().size());
        sensorMaintainerServices.deleteSensorMaintainersById(sensorsServices.getSensorById(sensor.getId()).getMaintainers().get(0).getId());
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

    @Test
    public void updateSensorsMaintainerDataBySensorId() {
        String name = sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getOwnerName();
        String surname = sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getOwnerSurname();
        String company = sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getCompany();
        String phoneNumber = sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getPhoneNumber();
        String mail = sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getMail();

        sensorMaintainerServices.updateSensorsMaintainerDataBySensorId(new Maintainer("TestName", "TestSurname", "Agency S.r.l.", "3693693693", "example@example.com"), id); 
        Assert.assertEquals("TestName", sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getOwnerName());
        Assert.assertEquals("TestSurname", sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getOwnerSurname());
        Assert.assertEquals("Agency S.r.l.", sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getCompany());
        Assert.assertEquals("3693693693", sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getPhoneNumber());
        Assert.assertEquals("example@example.com", sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getMail());

        sensorMaintainerServices.updateSensorsMaintainerDataBySensorId(new Maintainer(null, null, null , null, null), id);
        Assert.assertNotNull(sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getOwnerName());
        Assert.assertNotNull(sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getOwnerSurname());
        Assert.assertNotNull(sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getCompany());
        Assert.assertNotNull(sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getPhoneNumber());
        Assert.assertNotNull(sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getMail());

        sensorMaintainerServices.updateSensorsMaintainerDataBySensorId(new Maintainer(name, surname, company , phoneNumber, mail), id);
        Assert.assertEquals(name, sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getOwnerName());
        Assert.assertEquals(surname, sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getOwnerSurname());
        Assert.assertEquals(company, sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getCompany());
        Assert.assertEquals(phoneNumber, sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getPhoneNumber());
        Assert.assertEquals(mail, sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getMail());
    }

    @Test
    public void updateSensorsMaintainerDataById() {
        Long maintainerId = sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getId();
        String name = sensorMaintainerServices.getSensorsMaintainerDataById(maintainerId).getOwnerName();
        String surname = sensorMaintainerServices.getSensorsMaintainerDataById(maintainerId).getOwnerSurname();
        String company = sensorMaintainerServices.getSensorsMaintainerDataById(maintainerId).getCompany();
        String phoneNumber = sensorMaintainerServices.getSensorsMaintainerDataById(maintainerId).getPhoneNumber();
        String mail = sensorMaintainerServices.getSensorsMaintainerDataById(maintainerId).getMail();

        sensorMaintainerServices.updateSensorsMaintainerDataById(new Maintainer("TestName", "TestSurname", "Agency S.r.l.", "3693693693", "example@example.com"), id); 
        Assert.assertEquals("TestName", sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getOwnerName());
        Assert.assertEquals("TestSurname", sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getOwnerSurname());
        Assert.assertEquals("Agency S.r.l.", sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getCompany());
        Assert.assertEquals("3693693693", sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getPhoneNumber());
        Assert.assertEquals("example@example.com", sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getMail());

        sensorMaintainerServices.updateSensorsMaintainerDataById(new Maintainer(null, null, null , null, null), id);
        Assert.assertNotNull(sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getOwnerName());
        Assert.assertNotNull(sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getOwnerSurname());
        Assert.assertNotNull(sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getCompany());
        Assert.assertNotNull(sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getPhoneNumber());
        Assert.assertNotNull(sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getMail());

        sensorMaintainerServices.updateSensorsMaintainerDataById(new Maintainer(name, surname, company , phoneNumber, mail), id);
        Assert.assertEquals(name, sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getOwnerName());
        Assert.assertEquals(surname, sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getOwnerSurname());
        Assert.assertEquals(company, sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getCompany());
        Assert.assertEquals(phoneNumber, sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getPhoneNumber());
        Assert.assertEquals(mail, sensorMaintainerServices.getSensorsMaintainerDataBySensorId(id).get(0).getMail());
    }
}
