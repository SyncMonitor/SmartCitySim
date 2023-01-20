package it.synclab.smartcitysim.parkingspot;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.synclab.smartcitysim.exception.NotFoundException;
import it.synclab.smartcitysim.mapstruct.mappers.MapStructMapper;
import it.synclab.smartcitysim.parkingarea.ParkingAreaService;
import it.synclab.smartcitysim.parkingarea.dtos.ParkingAreaDto;
import it.synclab.smartcitysim.parkingarea.dtos.ParkingAreaWithParkingSpotsDto;
import it.synclab.smartcitysim.parkingarea.entities.ParkingArea;
import it.synclab.smartcitysim.parkingspot.dtos.ParkingSpotDto;
import it.synclab.smartcitysim.parkingspot.entities.ParkingSpot;
import it.synclab.smartcitysim.sensor.SensorService;
import it.synclab.smartcitysim.sensor.dtos.SensorDto;
import it.synclab.smartcitysim.sensor.dtos.SensorWithParkingSpotsDto;
import it.synclab.smartcitysim.sensor.entities.Sensor;

// TODO: fix integrity constraint error and show the detail error
@Service
public class ParkingSpotService{

    @Autowired
    private ParkingAreaService parkingAreaService;

    @Autowired 
    private SensorService sensorService;

    @Autowired
    protected ParkingSpotRepository repository;

    @Autowired
    protected MapStructMapper mapStructMapper;

    public List<ParkingSpotDto> getAll(){
        List<ParkingSpot> parkingSpots = repository.findAll();

        List<ParkingSpotDto> parkingSpotsDto = mapStructMapper.parkingSpotsToParkingSpotsDto(parkingSpots);

        return parkingSpotsDto;
    }

    public ParkingSpotDto getById(Long id) throws NotFoundException{
        Optional<ParkingSpot> parkingSpot = repository.findById(id);
        
        if(!parkingSpot.isPresent()){
            throw new NotFoundException("Object id not found");
        }

        ParkingSpotDto parkingSpotDto = mapStructMapper.parkingSpotToParkingSpotDto(parkingSpot.get());

        return parkingSpotDto;
    }

    // TODO: move this in a separate controller in Sensor domain
    public List<SensorDto> getAllSensorsById(Long id){
        getById(id);

        ParkingSpot parkingSpot = repository.findById(id).get();
        List<Sensor> sensors = parkingSpot.getSensors();
        List<SensorDto> sensorsDto = mapStructMapper.sensorsToSensorsDto(sensors);

        return sensorsDto;
    }

    public List<ParkingSpotDto> getAllByParkingAreaId(Long id){
        ParkingAreaWithParkingSpotsDto parkingAreaWithParkingSpotsDto = 
            parkingAreaService.getByIdWithParkingSpot(id);

        List<ParkingSpot> parkingSpots = parkingAreaWithParkingSpotsDto.getParkingSpots();
        List<ParkingSpotDto> parkingSpotsDto = mapStructMapper.parkingSpotsToParkingSpotsDto(parkingSpots);

        return parkingSpotsDto;
    }

    public List<ParkingSpotDto> getAllBySensorId(Long id){
        SensorWithParkingSpotsDto sensorWithParkingSpotsDto =
            sensorService.getByIdWithParkingSpots(id);

        List<ParkingSpot> parkingSpots = sensorWithParkingSpotsDto.getParkingSpots();
        List<ParkingSpotDto> parkingSpotsDto = mapStructMapper.parkingSpotsToParkingSpotsDto(parkingSpots);

        return parkingSpotsDto;
    }

    public ParkingSpotDto create(Long parkingAreaId, ParkingSpotDto parkingSpotDto){
        
        ParkingAreaDto parkingAreaDto = parkingAreaService.getById(parkingAreaId);
        ParkingArea parkingArea = mapStructMapper.parkingAreaDtoToParkingArea(parkingAreaDto);

        ParkingSpot parkingSpot = mapStructMapper.parkingSpotDtoToParkingSpot(parkingSpotDto);
        parkingSpot.setParkingArea(parkingArea);
        ParkingSpot parkingSpotSaved = repository.save(parkingSpot);
        ParkingSpotDto parkingSpotDtoSaved = mapStructMapper.parkingSpotToParkingSpotDto(parkingSpotSaved);

        return parkingSpotDtoSaved;
    }

    public ParkingSpotDto update(ParkingSpotDto parkingSpotDto) throws NotFoundException{
        Long id = parkingSpotDto.getId();

        getById(id);

        ParkingSpot parkingSpot = repository.findById(id).get();
        ParkingArea parkingArea = parkingSpot.getParkingArea();

        ParkingSpot parkingSpotToSave = mapStructMapper.parkingSpotDtoToParkingSpot(parkingSpotDto);
        parkingSpotToSave.setParkingArea(parkingArea);
        ParkingSpot parkingSpotSaved = repository.save(parkingSpotToSave);
        ParkingSpotDto parkingSpotDtoSaved = mapStructMapper.parkingSpotToParkingSpotDto(parkingSpotSaved);

        return parkingSpotDtoSaved;
    }

    public void delete(Long id) throws NotFoundException {
        getById(id);

        repository.deleteById(id);
    }
}
