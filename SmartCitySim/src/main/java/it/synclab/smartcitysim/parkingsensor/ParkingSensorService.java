package it.synclab.smartcitysim.parkingsensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import it.synclab.smartcitysim.exception.NotFoundException;
import it.synclab.smartcitysim.mapstruct.mappers.MapStructMapper;
import it.synclab.smartcitysim.parkingsensor.dtos.ParkingSensorDto;
import it.synclab.smartcitysim.parkingsensor.dtos.ParkingSensorWithSensorDto;
import it.synclab.smartcitysim.parkingsensor.dtos.ParkingSensorWithSensorIdDto;
import it.synclab.smartcitysim.parkingsensor.entities.ParkingSensor;
import it.synclab.smartcitysim.sensor.SensorService;
import it.synclab.smartcitysim.sensor.dtos.SensorDto;
import it.synclab.smartcitysim.sensor.dtos.SensorWithParkingSensorDto;
import it.synclab.smartcitysim.sensor.entities.Sensor;

@Service
public class ParkingSensorService{

    @Autowired
    private SensorService sensorService;

    @Autowired
    protected ParkingSensorRepository repository;

    @Autowired
    protected MapStructMapper mapStructMapper;

    public List<ParkingSensorDto> getAll(){
        List<ParkingSensor> parkingSensors = repository.findAll();

        List<ParkingSensorDto> parkingSensorsDto = mapStructMapper.parkingSensorsToParkingSensorsDto(parkingSensors);

        return parkingSensorsDto;
    }

    public ParkingSensorDto getById(Long id) throws NotFoundException{
        Optional<ParkingSensor> parkingSensor = repository.findById(id);
        
        if(!parkingSensor.isPresent()){
            throw new NotFoundException("Object id not found");
        }

        ParkingSensorDto parkingSensorDto = mapStructMapper.parkingSensorToParkingSensorDto(parkingSensor.get());

        return parkingSensorDto;
    }

    public ParkingSensorDto getBySensorId(Long id){
        SensorWithParkingSensorDto sensorWithParkingSensorDto = sensorService.getByIdWithParkingSensor(id);

        ParkingSensor parkingSensor = sensorWithParkingSensorDto.getParkingSensor();
        ParkingSensorDto parkingSensorDto = mapStructMapper.parkingSensorToParkingSensorDto(parkingSensor);

        return parkingSensorDto;
    }

    public ParkingSensorDto create(ParkingSensorDto parkingSensorDto){
        
        ParkingSensor parkingSensor = mapStructMapper.parkingSensorDtoToParkingSensor(parkingSensorDto);
        ParkingSensor parkingSensorSaved = repository.save(parkingSensor);
        ParkingSensorDto parkingSensorDtoSaved = mapStructMapper.parkingSensorToParkingSensorDto(parkingSensorSaved);

        return parkingSensorDtoSaved;
    }

    public List<ParkingSensorDto> create(List<ParkingSensorWithSensorIdDto> parkingSensorsWithSensorIdDto){
        List<ParkingSensorWithSensorDto> parkingSensorsWithSensorDto = 
            sensorIdToSensor(parkingSensorsWithSensorIdDto);

        List<ParkingSensor> updatedParkingSensors = mapStructMapper
            .parkingSensorsWithSensorDtoToParkingSensors(parkingSensorsWithSensorDto);

        List<ParkingSensor> currentParkingSensors = repository.findAll();

        updatedParkingSensors = updateCurrentParkingSensors(currentParkingSensors, updatedParkingSensors);

        List<ParkingSensor> parkingSensorsSaved = Lists.newArrayList(repository.saveAll(updatedParkingSensors));
        List<ParkingSensorDto> parkingSensorsDtoSaved = mapStructMapper.parkingSensorsToParkingSensorsDto(parkingSensorsSaved);

        return parkingSensorsDtoSaved;
    }

    public ParkingSensorDto update(ParkingSensorDto parkingSensorDto) throws NotFoundException{
        Long id = parkingSensorDto.getId();

        getById(id);

        ParkingSensor parkingSensor = mapStructMapper.parkingSensorDtoToParkingSensor(parkingSensorDto);
        ParkingSensor parkingSensorSaved = repository.save(parkingSensor);
        ParkingSensorDto parkingSensorDtoSaved = mapStructMapper.parkingSensorToParkingSensorDto(parkingSensorSaved);

        return parkingSensorDtoSaved;
    }

    public void delete(Long id) throws NotFoundException {
        getById(id);

        repository.deleteById(id);
    }

    private List<ParkingSensorWithSensorDto> sensorIdToSensor(
        List<ParkingSensorWithSensorIdDto> parkingSensorsWithSensorIdDto
    ){
        List<ParkingSensorWithSensorDto> parkingSensorsWithSensorDto = 
            new ArrayList<>();

        for(ParkingSensorWithSensorIdDto parkingSensorWithSensorIdDto: 
            parkingSensorsWithSensorIdDto){
                ParkingSensorWithSensorDto parkingSensorWithSensorDto = 
                    new ParkingSensorWithSensorDto();
                    
                Long sensorId = parkingSensorWithSensorIdDto.getSensorId();
                SensorDto sensorDto = sensorService.getById(sensorId);
                Sensor sensor = mapStructMapper.sensorDtoToSensor(sensorDto);

                parkingSensorWithSensorDto = 
                    setParkingSensorWithSensorDto(parkingSensorWithSensorDto, parkingSensorWithSensorIdDto);

                parkingSensorWithSensorDto.setSensor(sensor);

                parkingSensorsWithSensorDto.add(parkingSensorWithSensorDto);
        }

        return parkingSensorsWithSensorDto;
    }

    private ParkingSensorWithSensorDto setParkingSensorWithSensorDto(
        ParkingSensorWithSensorDto parkingSensorWithSensorDto, 
        ParkingSensorWithSensorIdDto parkingSensorWithSensorIdDto
    ){
        parkingSensorWithSensorDto.setAddress(parkingSensorWithSensorIdDto.getAddress());
        parkingSensorWithSensorDto.setLatitude(parkingSensorWithSensorIdDto.getLatitude());
        parkingSensorWithSensorDto.setLongitude(parkingSensorWithSensorIdDto.getLongitude());
        parkingSensorWithSensorDto.setValue(parkingSensorWithSensorIdDto.getValue());

        ParkingSensorDto parkingSensorDto = this.getBySensorId(parkingSensorWithSensorIdDto.getSensorId());
        if(parkingSensorDto != null){
            parkingSensorWithSensorDto.setId(parkingSensorDto.getId());
        }

        return parkingSensorWithSensorDto;
    }

    private List<ParkingSensor> updateCurrentParkingSensors(
        List<ParkingSensor> currentParkingSensors, 
        List<ParkingSensor> updatedParkingSensors
    ){
        Integer index;
        for(ParkingSensor updatedParkingSensor: updatedParkingSensors){
            index = updatedParkingSensor.getId() != null 
                ? hasSameElementId(updatedParkingSensor, currentParkingSensors)
                : -1;
            if(index == -1){
                currentParkingSensors.add(updatedParkingSensor);
            }
            else{
                currentParkingSensors.set(
                    index, 
                    updateCurrentParkingSensor(currentParkingSensors.get(index), updatedParkingSensor)
                );
            }
        }

        return currentParkingSensors;
    }

    private Integer hasSameElementId(ParkingSensor parkingSensor, List<ParkingSensor> parkingSensors){
        Integer i = 0;
        for(ParkingSensor p : parkingSensors){
            if(parkingSensor.getId().equals(p.getId()))
                return i;
            i++;
        }
        return -1;
    }

    private ParkingSensor updateCurrentParkingSensor(
        ParkingSensor currentParkingSensor, 
        ParkingSensor updatedParkingSensor
    ){
        currentParkingSensor.setAddress(updatedParkingSensor.getAddress());
        currentParkingSensor.setLatitude(updatedParkingSensor.getLatitude());
        currentParkingSensor.setLongitude(updatedParkingSensor.getLongitude());
        currentParkingSensor.setValue(updatedParkingSensor.getValue());

        return currentParkingSensor;
    }
}
