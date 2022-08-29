package it.synclab.smartparking;


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
import it.synclab.smartparking.repository.model.ParkingArea;
import it.synclab.smartparking.repository.model.Sensor;
import it.synclab.smartparking.service.MailServices;
import it.synclab.smartparking.service.SensorServices;
import it.synclab.smartparking.service.StartUpServices;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServicesTest {

    @Autowired
    private MailServices mailServices;

    @Autowired
    private StartUpServices startUpServices;

    @Autowired
    private SensorServices sensorServices;

    Sensor sensor;
    ParkingArea parkingArea;
    List<ParkingArea> list = new ArrayList<>();
    Long id = 0L;

    @Before
    public void init() {
        parkingArea = new ParkingArea("45.00000", "24.00000", "Sensor address", false, null);
        list.add(parkingArea);
        sensor = new Sensor(1234567890L, "TestSensor", "TestAddress", "T", "TestType", true, list);
        id = sensorServices.getAllSensorsFromDB().get(0).getId();
    }

    @Test
    public void printMailTest() {
        String expected = "Id = " + sensor.getId() + ", Name = " + sensor.getName() + ", " + "Address = "
                + sensor.getParkingArea().get(0).getAddress() + ", Latitude = "
                + sensor.getParkingArea().get(0).getLatitude() + ", Longitude = "
                + sensor.getParkingArea().get(0).getLongitude();
        String result = mailServices.printMail(sensor, sensor.getParkingArea());
        Assert.assertEquals(expected, result);
    }

    // @Transactional
    // @Test
    // public void sendSensorOffMailTest() {
    //     MarkerList sensors = new MarkerList();
    //     try {
    //         sensors = startUpServices.readDataFromSources();
    //         sensors.getMarkers().getMarkers().get(0).setId(id);
    //         sensors.getMarkers().getMarkers().get(9).setName("TestName");
    //         sensors.getMarkers().getMarkers().get(9).setAddress("TestAddress");
    //         sensors.getMarkers().getMarkers().get(9).setLat("TestLat");
    //         sensors.getMarkers().getMarkers().get(9).setLng("TestLng");
    //         sensors.getMarkers().getMarkers().get(9).setBattery("3,7V");
    //         sensors.getMarkers().getMarkers().get(9).setActive(false);
    //         sensors.getMarkers().getMarkers().get(9).setBattery("1,3V");
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     startUpServices.updateDBData(sensors);
    //     Assert.assertTrue(sensorServices.getSensorById(id).getMaintainer().get(0).isToBeRepaired());
    //     mailServices.sendLowBatterySensorsMail();
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
        Assert.assertTrue(sensorServices.getSensorById(id).getMaintainer().get(0).isToBeCharged());
        mailServices.sendLowBatterySensorsMail();
    }
}
