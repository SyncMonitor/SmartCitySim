package it.synclab.smartcitysim.sensorpolling;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import it.synclab.smartcitysim.mapstruct.mappers.MapStructMapper;
import it.synclab.smartcitysim.parkingsensor.ParkingSensorService;
import it.synclab.smartcitysim.parkingsensor.dtos.ParkingSensorDto;
import it.synclab.smartcitysim.parkingsensor.dtos.ParkingSensorWithSensorIdDto;
import it.synclab.smartcitysim.sensor.SensorService;
import it.synclab.smartcitysim.sensor.dtos.SensorDto;
import it.synclab.smartcitysim.sensorpolling.dtos.SensorPollingDto;
import it.synclab.smartcitysim.sensorpolling.dtos.SensorsPollingDto;

@Service
public class SensorPollingService {
    
    @Autowired
    private SensorService sensorService;

    @Autowired
    private ParkingSensorService parkingSensorService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private XmlMapper xmlMapper;

    @Autowired
    private MapStructMapper mapStructMapper;

    private String url = "https://syncmonitor.altervista.org/smartcitysim/smartParkingSensors.xml";

    public List<SensorPollingDto> pollSensors() throws IOException{
        String sensorPollingXml = 
            restTemplate.getForObject(url, String.class);
        
        SensorsPollingDto sensorPollingDto = xmlMapper.readValue(sensorPollingXml, SensorsPollingDto.class);
        
        return sensorPollingDto.getSensorsPollingDto();
    }

    @Scheduled(fixedDelay = 120000)
    public void persistSensors() throws IOException{
        System.out.println("Sensors polling started...");

        List<SensorPollingDto> sensorsPollingDto = pollSensors();

        List<SensorDto> sensorsDto = mapStructMapper.sensorsPollingDtoToSensorDto(sensorsPollingDto);

        List<ParkingSensorWithSensorIdDto> parkingSensorsWithSensorIdDto = mapStructMapper
            .sensorsPollingDtoToParkingSensorsWithSensorIdDto(sensorsPollingDto);

        List<SensorDto> sensorsDtoSaved = sensorService.create(sensorsDto);

        List<ParkingSensorDto> parkingSensorsDto = parkingSensorService.create(parkingSensorsWithSensorIdDto);

        System.out.println("Sensors polling finished...");
    }
}
