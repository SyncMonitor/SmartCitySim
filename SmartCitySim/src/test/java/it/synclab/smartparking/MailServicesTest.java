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

import it.synclab.smartparking.repository.model.ParkingArea;
import it.synclab.smartparking.repository.model.Sensor;
import it.synclab.smartparking.service.MailServices;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServicesTest {

    @Autowired
    private MailServices mailServices;

    Sensor sensor;
    ParkingArea parkingArea;
    List<ParkingArea> list = new ArrayList<>();

    @Before
    public void init() {
        parkingArea = new ParkingArea("45.00000", "24.00000", "Sensor address", false, null);
        list.add(parkingArea);
        sensor = new Sensor(1234567890L, "TestSensor", "TestAddress", "T", "TestType", true, list);
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
}
