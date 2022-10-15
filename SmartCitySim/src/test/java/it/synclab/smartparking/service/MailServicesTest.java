package it.synclab.smartparking.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.synclab.smartparking.model.MarkerList;
import it.synclab.smartparking.repository.model.ParkingSensors;
import it.synclab.smartparking.repository.model.Sensors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServicesTest {

    @Autowired
    private MailServices mailServices;

    @Autowired
    private StartUpServices startUpServices;

    @Autowired
    private SensorsServices sensorsServices;

    Sensors sensor;
    ParkingSensors parkingSensor;
    List<ParkingSensors> list = new ArrayList<>();
    Long id = 0L;

    @Before
    public void init() {
    	parkingSensor = new ParkingSensors("45.00000", "24.00000", "Sensor address", false, null);
        list.add(parkingSensor);
        sensor = new Sensors(1234567890L, "TestSensor", "TestAddress", "T", "TestType", true, parkingSensor);
        id = sensorsServices.getAllSensorsFromDB().get(0).getId();
    }

    @Test
    public void printMailTest() {
        String expected = "Id = " + sensor.getId() + ", Name = " + sensor.getName() + ", " + "Address = "
                + sensor.getParkingSensors().getAddress() + ", Latitude = "
                + sensor.getParkingSensors().getLatitude() + ", Longitude = "
                + sensor.getParkingSensors().getLongitude();
        String result = mailServices.printMail(sensor, sensor.getParkingSensors());
        Assert.assertEquals(expected, result);
    }

    // @Transactional
    // @Test
    // public void sendSensorOffMailTest() {
    // MarkerList sensors = new MarkerList();
    // try {
    // sensors = startUpServices.readDataFromSources();
    // sensors.getMarkers().getMarkers().get(0).setId(id);
    // sensors.getMarkers().getMarkers().get(9).setName("TestName");
    // sensors.getMarkers().getMarkers().get(9).setAddress("TestAddress");
    // sensors.getMarkers().getMarkers().get(9).setLat("TestLat");
    // sensors.getMarkers().getMarkers().get(9).setLng("TestLng");
    // sensors.getMarkers().getMarkers().get(9).setBattery("3,7V");
    // sensors.getMarkers().getMarkers().get(9).setActive(false);
    // sensors.getMarkers().getMarkers().get(9).setBattery("1,3V");
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // startUpServices.updateDBData(sensors);
    // Assert.assertTrue(sensorServices.getSensorById(id).getMaintainer().get(0).isToBeRepaired());
    // mailServices.sendLowBatterySensorsMail();
    // }

    @Transactional
    @Test
    public void sendLowBatterySensorsMailTest() {
        MarkerList sensors = new MarkerList();
        try {
            sensors = startUpServices.readDataFromSources();
            sensors.getMarkers().getMarkers().get(9).setId(id);
            sensors.getMarkers().getMarkers().get(9).setName("TestName");
            sensors.getMarkers().getMarkers().get(9).setAddress("TestAddress");
            sensors.getMarkers().getMarkers().get(9).setLat("TestLat");
            sensors.getMarkers().getMarkers().get(9).setLng("TestLng");
            sensors.getMarkers().getMarkers().get(9).setBattery("2,0V");
            sensors.getMarkers().getMarkers().get(9).setActive(true);
            sensors.getMarkers().getMarkers().get(9).setBattery("1,3V");
        } catch (Exception e) {
            e.printStackTrace();
        }
        startUpServices.updateDBData(sensors);
        Assert.assertTrue(sensorsServices.getSensorById(id).getMaintainers().get(0).isToBeCharged());
        mailServices.sendLowBatterySensorsMail();
    }
}
