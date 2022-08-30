package it.synclab.smartparking.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.synclab.smartparking.model.Marker;
import it.synclab.smartparking.model.MarkerList;
import it.synclab.smartparking.model.Markers;
import it.synclab.smartparking.repository.model.Sensor;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StartUpServicesTest {

    @Autowired
    private StartUpServices startUpServices;

    @Autowired
    private SensorServices sensorServices;

    @Autowired
    private ParkingAreaServices parkingAreaServices;

    @Autowired
    private SensorMaintainerServices sensorMaintainersServices;

    Markers markers = new Markers();
    MarkerList expected = new MarkerList();
    Long id = 0L;

    String xml = "";

    @Before
    public void init() {
        xml = "<markers>"
                + "<marker id=\"1\" name=\"TestName1\" address=\"Padova Galleria Spagna\" lat=\"45.389041\" lng=\"11.928577\" state=\"0\" battery=\"3,7V\" active=\"1\"/>"
                + "<marker id=\"2\" name=\"TestName2\" address=\"Padova Prato della valle\" lat=\"45.397838\" lng=\"11.875102\" state=\"1\" battery=\"2,7V\" active=\"0\"/>"
                + "</markers>";

        markers.getMarkers().add(
                new Marker(1L, "156A2C71", "Padova Galleria Spagna", "45.389040", "11.928577", false, "3,7V", true));
        markers.getMarkers().add(
                new Marker(2L, "156A2A71", "Padova Galleria Spagna", "45.389029", "11.928598", true, "2,7V", false));
        id = sensorServices.getAllSensorsFromDB().get(0).getId();
    }

    @Test
    public void startUpServicesTest() {
        MarkerList markerList = startUpServices.convertXMLtoJson(xml);
        Assert.assertEquals(2, markerList.getMarkers().getMarkers().size());
        Marker result1 = markerList.getMarkers().getMarkers().get(0);
        Marker result2 = markerList.getMarkers().getMarkers().get(1);
        Assert.assertEquals("1", result1.getId().toString());
        Assert.assertEquals("TestName1", result1.getName());
        Assert.assertEquals("Padova Galleria Spagna", result1.getAddress());
        Assert.assertEquals("45.389041", result1.getLat());
        Assert.assertEquals("11.928577", result1.getLng());
        Assert.assertEquals(false, result1.getState());
        Assert.assertEquals("3,7V", result1.getBattery());
        Assert.assertEquals(true, result1.isActive());
        Assert.assertEquals("2", result2.getId().toString());
        Assert.assertEquals("TestName2", result2.getName());
        Assert.assertEquals("Padova Prato della valle", result2.getAddress());
        Assert.assertEquals("45.397838", result2.getLat());
        Assert.assertEquals("11.875102", result2.getLng());
        Assert.assertEquals(true, result2.getState());
        Assert.assertEquals("2,7V", result2.getBattery());
        Assert.assertEquals(false, result2.isActive());
    }

    @Test
    public void readDataFromSourcesTest() {
        MarkerList result = new MarkerList();
        Marker result1 = new Marker();
        Marker result2 = new Marker();
        Marker result15 = new Marker();
        try {
            result = startUpServices.readDataFromSources();
            result1 = result.getMarkers().getMarkers().get(0);
            result2 = result.getMarkers().getMarkers().get(1);
            result15 = result.getMarkers().getMarkers().get(14);
        } catch (Exception e) {
            Assert.assertEquals("No data found", e.getMessage());
        }
        Assert.assertEquals(15, result.getMarkers().getMarkers().size());

        Assert.assertEquals("1", result1.getId().toString());
        Assert.assertEquals("2", result2.getId().toString());
        Assert.assertEquals("15", result15.getId().toString());

    }

    @Transactional
    @Test
    public void updateSensorsDataTest() {
        int size = 0;
        try {
            size = startUpServices.readDataFromSources().getMarkers().getMarkers().size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sensorMaintainersServices.deleteAllSensorMaintainers();
        parkingAreaServices.deleteAllParkingArea();
        sensorServices.deleteAllSensors();

        startUpServices.updateSensorsData();

        Assert.assertEquals(size, sensorServices.getAllSensorsFromDB().size());
    }

    @Test
    public void updateDBDataTest() {
        MarkerList sensors = new MarkerList();
        try {
            sensors = startUpServices.readDataFromSources();
            sensors.getMarkers().getMarkers().get(9).setId(id);
            sensors.getMarkers().getMarkers().get(9).setName("TestName");
            sensors.getMarkers().getMarkers().get(9).setAddress("TestAddress");
            sensors.getMarkers().getMarkers().get(9).setLat("TestLat");
            sensors.getMarkers().getMarkers().get(9).setLng("TestLng");
            sensors.getMarkers().getMarkers().get(9).setBattery("2,0V");
            sensors.getMarkers().getMarkers().get(9).setActive(false);
            sensors.getMarkers().getMarkers().get(8).setBattery("1,3V");
        } catch (Exception e) {
            e.printStackTrace();
        }
        startUpServices.updateDBData(sensors);
        Assert.assertEquals(sensors.getMarkers().getMarkers().get(9).getName(),
                sensorServices.getSensorById(id).getName());
        try {
            sensors = startUpServices.readDataFromSources();
        } catch (Exception e) {
            e.printStackTrace();
        }
        startUpServices.updateDBData(sensors);
        Assert.assertEquals(sensors.getMarkers().getMarkers().get(9).getBattery(),
                sensorServices.getSensorById(id).getBattery());
    }

    @Transactional
    @Test
    public void SensorNotUpdatingFromMoreFiveDaysTest() {
        Sensor sensor = new Sensor();
        sensor = sensorServices.getSensorById(id);
        sensor.setLastSurvey(LocalDateTime.now().minusDays(6));
        MarkerList sensors = new MarkerList();
        try {
            sensors = startUpServices.readDataFromSources();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sensorServices.saveSensorData(sensor);
        startUpServices.updateDBData(sensors);
        Assert.assertEquals(sensorServices.getSensorById(id).getLastSurvey(),
                sensorServices.getSensorById(id).getLastSurvey());
    }

    @Transactional
    @Test
    public void writeSensorsIfAddedTest() {
        MarkerList sensors = new MarkerList();
        try {
            sensors = startUpServices.readDataFromSources();
            sensors.getMarkers().getMarkers()
                    .add(new Marker(1234567890123456789L, "TEST", "TEST", "TEST", "TEST", false, "TEST", false));
        } catch (Exception e) {
            e.printStackTrace();
        }
        startUpServices.writeSensorsIfAdded(sensors);
        Assert.assertEquals(sensors.getMarkers().getMarkers().size(), sensorServices.getAllSensorsFromDB().size());
    }

    @Test
    public void readParkingAreaData() {
        MarkerList parkingArea = new MarkerList();
        try {
            parkingArea = startUpServices.readParkingAreaData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue("uncorrect reading or reading from an empty file.",
                parkingArea.getMarkers().getMarkers().size() > 0);
    }
}
