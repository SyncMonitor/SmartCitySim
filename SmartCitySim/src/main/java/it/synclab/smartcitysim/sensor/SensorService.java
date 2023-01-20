package it.synclab.smartcitysim.sensor;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import it.synclab.smartcitysim.exception.NotFoundException;
import it.synclab.smartcitysim.mapstruct.mappers.MapStructMapper;
import it.synclab.smartcitysim.sensor.dtos.SensorDto;
import it.synclab.smartcitysim.sensor.dtos.SensorWithMaintenanceDto;
import it.synclab.smartcitysim.sensor.dtos.SensorWithParkingSensorDto;
import it.synclab.smartcitysim.sensor.dtos.SensorWithParkingSpotsDto;
import it.synclab.smartcitysim.sensor.entities.Sensor;
import it.synclab.smartcitysim.sensormaintenance.SensorMaintenanceService;
import it.synclab.smartcitysim.sensormaintenance.dtos.SensorMaintenanceWithSensorDto;

/**
 * NOTE: the getById is used also inside other methods
 * just to thrown NotFoundException if id wasn't found
 */

@Service
public class SensorService{

    @Autowired
    private SensorMaintenanceService sensorMaintenanceService;

    @Autowired
    protected SensorRepository repository;

    @Autowired
    protected MapStructMapper mapStructMapper;

    

    public List<SensorDto> getAll(){
        List<Sensor> sensors = repository.findAll();
        
        List<SensorDto> sensorsDto = mapStructMapper.sensorsToSensorsDto(sensors);

        return sensorsDto;
    }

    public SensorDto getById(Long id) throws NotFoundException{
        Optional<Sensor> sensor = repository.findById(id);
        
        if(!sensor.isPresent()){
            throw new NotFoundException("Object id not found");
        }

        SensorDto sensorDto = mapStructMapper.sensorToSensorDto(sensor.get());

        return sensorDto;
    }

    //TODO: create also a sensor maintenance if not exists
    public SensorDto create(SensorDto sensorDto){

        Sensor sensor = mapStructMapper.sensorDtoToSensor(sensorDto);
        Sensor sensorSaved = repository.save(sensor);
        SensorDto sensorDtoSaved = mapStructMapper.sensorToSensorDto(sensorSaved);

        return sensorDtoSaved;
    }

    public List<SensorDto> create(List<SensorDto> sensorsDto){

        List<Sensor> updatedSensors = mapStructMapper.sensorsDtoToSensors(sensorsDto);

        List<Sensor> currentSensors = repository.findAll();
        
        updatedSensors = updateCurrentSensors(currentSensors, updatedSensors);
        List<Sensor> sensorsWithoutMaintenance = addMaintenanceToSensors();

        List<Sensor> sensorsSaved = Lists.newArrayList(repository.saveAll(updatedSensors));
        List<SensorDto> sensorsDtoSaved = mapStructMapper.sensorsToSensorsDto(sensorsSaved);

        return sensorsDtoSaved;
    }

    public SensorDto update(SensorDto sensorDto) throws NotFoundException{
        Long id = sensorDto.getId();

        getById(id);

        Sensor sensor = mapStructMapper.sensorDtoToSensor(sensorDto);
        Sensor sensorSaved = repository.save(sensor);
        SensorDto sensorDtoSaved = mapStructMapper.sensorToSensorDto(sensorSaved);

        return sensorDtoSaved;
    }

    public void delete(Long id) throws NotFoundException {
        getById(id);

        repository.deleteById(id);
    }

    public List<SensorWithMaintenanceDto> getAllWithSensorMaintenance(){
        List<Sensor> sensors = repository.findAll();
        List<SensorWithMaintenanceDto> sensorsWithMaintenanceDto = mapStructMapper.sensorsToSensorsWithMaintenanceDto(sensors);

        return sensorsWithMaintenanceDto;
    }

    public SensorWithMaintenanceDto getByIdWithSensorMaintenance(Long id){
        getById(id);

        Sensor sensor = repository.findById(id).get();
        SensorWithMaintenanceDto sensorWithMaintenanceDto = mapStructMapper.sensorToSensorWithMaintenanceDto(sensor);
        
        return sensorWithMaintenanceDto;
    }

    public SensorWithParkingSpotsDto getByIdWithParkingSpots(Long id){
        getById(id);

        Sensor sensor = repository.findById(id).get();
        
        SensorWithParkingSpotsDto sensorWithParkingSpotsDto = mapStructMapper.sensorToSensorWithParkingSpotDto(sensor);

        return sensorWithParkingSpotsDto;
    }

    public SensorWithParkingSensorDto getByIdWithParkingSensor(Long id){
        getById(id);

        Sensor sensor = repository.findById(id).get();

        SensorWithParkingSensorDto sensorWithParkingSensorDto = 
            mapStructMapper.sensorToSensorWithParkingSensorDto(sensor);

        return sensorWithParkingSensorDto;
    }

    private List<Sensor> updateCurrentSensors(List<Sensor> currentSensors, List<Sensor> updatedSensors){
        Integer index;
        for(Sensor updatedSensor: updatedSensors){
            index = hasSameElementId(updatedSensor, currentSensors);
            if(index == -1){
                currentSensors.add(updatedSensor);
            }
            else{
                currentSensors.set(index, updateCurrentSensor(currentSensors.get(index), updatedSensor));
            }
        }

        return currentSensors;
    }

    private Integer hasSameElementId(Sensor sensor, List<Sensor> sensors){
        Integer i = 0;
        for(Sensor s : sensors){
            if(sensor.getId().equals(s.getId()))
                return i;
            i++;
        }
        return -1;
    }

    private Sensor updateCurrentSensor(Sensor currentSensor, Sensor updatedSensor){
        currentSensor.setName(updatedSensor.getName());
        currentSensor.setBattery(updatedSensor.getBattery());
        currentSensor.setCharge(updatedSensor.getCharge());
        currentSensor.setType(updatedSensor.getType());
        currentSensor.setIsActive(updatedSensor.getIsActive());

        return currentSensor;
    }

    private List<Sensor> addMaintenanceToSensors(){
        List<Sensor> sensorsWithoutMaintenance = repository.getAllWithoutMaintenance();
        
        List<SensorMaintenanceWithSensorDto> sensorsMaintenanceWithSensorDto = this
            .sensorMaintenanceService.addMaintenanceToSensors(sensorsWithoutMaintenance);

        return sensorsWithoutMaintenance;
    }
}
