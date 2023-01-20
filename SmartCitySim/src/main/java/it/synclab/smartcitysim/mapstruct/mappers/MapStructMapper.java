package it.synclab.smartcitysim.mapstruct.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import it.synclab.smartcitysim.maintainerregistry.dtos.MaintainerRegistryDto;
import it.synclab.smartcitysim.maintainerregistry.entities.MaintainerRegistry;
import it.synclab.smartcitysim.parkingarea.dtos.ParkingAreaDto;
import it.synclab.smartcitysim.parkingarea.dtos.ParkingAreaWithParkingSpotsDto;
import it.synclab.smartcitysim.parkingarea.entities.ParkingArea;
import it.synclab.smartcitysim.parkingsensor.dtos.ParkingSensorDto;
import it.synclab.smartcitysim.parkingsensor.dtos.ParkingSensorWithSensorDto;
import it.synclab.smartcitysim.parkingsensor.dtos.ParkingSensorWithSensorIdDto;
import it.synclab.smartcitysim.parkingsensor.entities.ParkingSensor;
import it.synclab.smartcitysim.parkingspot.dtos.ParkingSpotDto;
import it.synclab.smartcitysim.parkingspot.entities.ParkingSpot;
import it.synclab.smartcitysim.sensor.dtos.SensorDto;
import it.synclab.smartcitysim.sensor.dtos.SensorWithMaintenanceDto;
import it.synclab.smartcitysim.sensor.dtos.SensorWithParkingSensorDto;
import it.synclab.smartcitysim.sensor.dtos.SensorWithParkingSpotsDto;
import it.synclab.smartcitysim.sensor.entities.Sensor;
import it.synclab.smartcitysim.sensormaintenance.dtos.SensorMaintenanceDto;
import it.synclab.smartcitysim.sensormaintenance.dtos.SensorMaintenanceWithSensorDto;
import it.synclab.smartcitysim.sensormaintenance.entities.SensorMaintenance;
import it.synclab.smartcitysim.sensorpolling.dtos.SensorPollingDto;

@Mapper(componentModel = "spring")
public interface MapStructMapper{

    // ParkingArea

    ParkingAreaDto parkingAreaToParkingAreaDto(ParkingArea parkingArea);

    List<ParkingAreaDto> parkingAreasToParkingAreasDto(List<ParkingArea> parkingAreas);

    ParkingArea parkingAreaDtoToParkingArea(ParkingAreaDto parkingAreaDto);

    ParkingAreaWithParkingSpotsDto parkingAreaToParkingAreaWithParkingSpotsDto(ParkingArea parkingArea);

    // MaintainerRegistry

    MaintainerRegistryDto maintainerRegistryToMaintainerRegistryDto(MaintainerRegistry maintainerRegistry);

    List<MaintainerRegistryDto> maintainersRegistryToMaintainersRegistryDto(List<MaintainerRegistry> maintainersRegistry);

    MaintainerRegistry maintainerRegistryDtoToMaintainerRegistry(MaintainerRegistryDto maintainerRegistryDto);

    // ParkingSensor

    ParkingSensorDto parkingSensorToParkingSensorDto(ParkingSensor parkingSensor);

    List<ParkingSensorDto> parkingSensorsToParkingSensorsDto(List<ParkingSensor> parkingSensors);

    ParkingSensor parkingSensorDtoToParkingSensor(ParkingSensorDto parkingSensorDto);

    @Mapping(source = "state", target = "value")
    @Mapping(source = "id", target = "sensorId")
    ParkingSensorWithSensorIdDto sensorPollingDtoToParkingSensorWithSensorIdDto(SensorPollingDto sensorPollingDto);

    List<ParkingSensorWithSensorIdDto> sensorsPollingDtoToParkingSensorsWithSensorIdDto(List<SensorPollingDto> sensorsPollingDto);

    List<ParkingSensor> parkingSensorsWithSensorDtoToParkingSensors(List<ParkingSensorWithSensorDto> parkingSensorsWithSensorDto);

    // ParkingSpot

    ParkingSpotDto parkingSpotToParkingSpotDto(ParkingSpot parkingSpot);

    List<ParkingSpotDto> parkingSpotsToParkingSpotsDto(List<ParkingSpot> parkingSpots);

    ParkingSpot parkingSpotDtoToParkingSpot(ParkingSpotDto parkingSpotDto);

    // Sensor

    SensorDto sensorToSensorDto(Sensor sensor);

    List<SensorDto> sensorsToSensorsDto(List<Sensor> sensors);

    Sensor sensorDtoToSensor(SensorDto sensorDto);

    SensorWithMaintenanceDto sensorToSensorWithMaintenanceDto(Sensor sensor);

    List<SensorWithMaintenanceDto> sensorsToSensorsWithMaintenanceDto(List<Sensor> sensors);

    List<Sensor> sensorsDtoToSensors(List<SensorDto> sensorsDto);

    Sensor sensorWithMaintenanceDtoToSensor(SensorWithMaintenanceDto sensorWithMaintenanceDto);

    SensorWithParkingSpotsDto sensorToSensorWithParkingSpotDto(Sensor sensor);

    SensorWithParkingSensorDto sensorToSensorWithParkingSensorDto(Sensor sensor);

    @Mapping(source = "active", target = "isActive")
    SensorDto sensorPollingDtoToSensorDto(SensorPollingDto sensorPollingDto);

    List<SensorDto> sensorsPollingDtoToSensorDto(List<SensorPollingDto> sensorPollingDto);

    // SensorMaintenance

    SensorMaintenanceDto sensorMaintenanceToSensorMaintenanceDto(SensorMaintenance sensorMaintenance);

    List<SensorMaintenanceDto> sensorsMaintenanceToSensorsMaintenanceDto(List<SensorMaintenance> sensorsMaintenance);

    SensorMaintenance sensorMaintenanceDtoToSensorMaintenance(SensorMaintenanceDto sensorMaintenanceDto);

    List<SensorMaintenance> sensorsMaintenanceWithSensorDtoToSensorsMaintenance(List<SensorMaintenanceWithSensorDto> sensorMaintenanceWithSensorDto);

    List<SensorMaintenanceWithSensorDto> sensorsMaintenanceToSensorsMaintenanceWithSensorDto(List<SensorMaintenance> sensorsMaintenance);
}
