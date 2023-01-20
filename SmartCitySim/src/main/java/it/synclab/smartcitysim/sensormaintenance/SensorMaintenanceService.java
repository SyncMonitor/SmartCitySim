package it.synclab.smartcitysim.sensormaintenance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import it.synclab.smartcitysim.exception.NotFoundException;
import it.synclab.smartcitysim.mapstruct.mappers.MapStructMapper;
import it.synclab.smartcitysim.sensor.SensorService;
import it.synclab.smartcitysim.sensor.dtos.SensorWithMaintenanceDto;
import it.synclab.smartcitysim.sensor.entities.Sensor;
import it.synclab.smartcitysim.sensormaintenance.dtos.SensorMaintenanceDto;
import it.synclab.smartcitysim.sensormaintenance.dtos.SensorMaintenanceWithSensorDto;
import it.synclab.smartcitysim.sensormaintenance.entities.SensorMaintenance;
import lombok.Getter;

@Service
public class SensorMaintenanceService {

    private SensorService sensorService;

    protected SensorMaintenanceRepository repository;

    protected MapStructMapper mapStructMapper;

    @Autowired
    SensorMaintenanceService(
        @Lazy SensorService sensorService,
        SensorMaintenanceRepository repository,
        MapStructMapper mapStructMapper
    ){
        this.sensorService = sensorService;
        this.repository = repository;
        this.mapStructMapper = mapStructMapper;
    }

    public List<SensorMaintenanceDto> getAll(){
        List<SensorMaintenance> sensorsMaintenance = repository.findAll();

        List<SensorMaintenanceDto> sensorsMaintenanceDto = mapStructMapper.sensorsMaintenanceToSensorsMaintenanceDto(sensorsMaintenance);

        return sensorsMaintenanceDto;
    }

    public SensorMaintenanceDto getById(Long id) throws NotFoundException{
        Optional<SensorMaintenance> sensorMaintenance = repository.findById(id);
        
        if(!sensorMaintenance.isPresent()){
            throw new NotFoundException("Object id not found");
        }

        SensorMaintenanceDto sensorMaintenanceDto = mapStructMapper.sensorMaintenanceToSensorMaintenanceDto(sensorMaintenance.get());

        return sensorMaintenanceDto;
    }

    public SensorMaintenanceDto create(SensorMaintenanceDto sensorMaintenanceDto){
        SensorMaintenance sensorMaintenance = mapStructMapper.sensorMaintenanceDtoToSensorMaintenance(sensorMaintenanceDto);

        SensorMaintenance sensorMaintenanceSaved = repository.save(sensorMaintenance);
        SensorMaintenanceDto sensorMaintenanceDtoSaved = mapStructMapper.sensorMaintenanceToSensorMaintenanceDto(sensorMaintenanceSaved);

        return sensorMaintenanceDtoSaved;
    }

    public List<SensorMaintenanceWithSensorDto> create(List<SensorMaintenanceWithSensorDto> sensorsMaintenanceWithSensorDto){
        List<SensorMaintenance> sensorsMaintenance = mapStructMapper
            .sensorsMaintenanceWithSensorDtoToSensorsMaintenance(sensorsMaintenanceWithSensorDto);
        
        List<SensorMaintenance> sensorsMaintenanceSaved = Lists.newArrayList(repository.saveAll(sensorsMaintenance));
        List<SensorMaintenanceWithSensorDto> sensorsMaintenanceWithSensorDtoSaved = mapStructMapper
            .sensorsMaintenanceToSensorsMaintenanceWithSensorDto(sensorsMaintenanceSaved);

        return sensorsMaintenanceWithSensorDtoSaved;
    }

    public SensorMaintenanceDto update(Long sensorId, SensorMaintenanceDto sensorMaintenanceDto) throws NotFoundException{
        
        SensorWithMaintenanceDto sensorWithMaintenanceDto = sensorService.getByIdWithSensorMaintenance(sensorId);
        Sensor sensor = mapStructMapper.sensorWithMaintenanceDtoToSensor(sensorWithMaintenanceDto);

        SensorMaintenance sensorMaintenance = mapStructMapper.sensorMaintenanceDtoToSensorMaintenance(sensorMaintenanceDto);
        
        SensorMaintenance sensorMaintenanceSensor = sensor.getSensorMaintenance();

        if(sensorMaintenanceSensor == null){
            throw new NotFoundException("Object id has not items associated");
        }

        sensorMaintenance.setId(sensorMaintenanceSensor.getId());
        sensorMaintenance.setSensor(sensor);

        SensorMaintenance sensorMaintenanceSaved = repository.save(sensorMaintenance);
        SensorMaintenanceDto sensorMaintenanceDtoSaved = mapStructMapper.sensorMaintenanceToSensorMaintenanceDto(sensorMaintenanceSaved);

        return sensorMaintenanceDtoSaved;
    }

    public void delete(Long id) throws NotFoundException {
        getById(id);

        repository.deleteById(id);
    }

    public List<SensorMaintenanceWithSensorDto> addMaintenanceToSensors(List<Sensor> sensors){
        List<SensorMaintenance> sensorsMaintenance = new ArrayList<>();

        for(Sensor sensor: sensors){
            SensorMaintenance sensorMaintenance = new SensorMaintenance();
            sensorMaintenance.setToBeRepaired(false);
            sensorMaintenance.setToBeCharged(false);
            sensorMaintenance.setIsUpdating(false);
            sensorMaintenance.setSensor(sensor);

            sensorsMaintenance.add(sensorMaintenance);
        }

        List<SensorMaintenanceWithSensorDto> sensorsMaintenanceWithSensorDto = mapStructMapper
            .sensorsMaintenanceToSensorsMaintenanceWithSensorDto(sensorsMaintenance);

        List<SensorMaintenanceWithSensorDto> sensorsMaintenanceWithSensorDtoSaved = this.create(sensorsMaintenanceWithSensorDto);

        return sensorsMaintenanceWithSensorDtoSaved;
    }
}
