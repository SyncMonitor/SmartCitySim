package it.synclab.smartcitysim.parkingarea;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.synclab.smartcitysim.exception.NotFoundException;
import it.synclab.smartcitysim.mapstruct.mappers.MapStructMapper;
import it.synclab.smartcitysim.parkingarea.dtos.ParkingAreaDto;
import it.synclab.smartcitysim.parkingarea.dtos.ParkingAreaWithParkingSpotsDto;
import it.synclab.smartcitysim.parkingarea.entities.ParkingArea;

@Service
public class ParkingAreaService{

    @Autowired
    protected ParkingAreaRepository repository;

    @Autowired
    protected MapStructMapper mapStructMapper;

    public List<ParkingAreaDto> getAll(){
        List<ParkingArea> parkingAreas = repository.findAll();

        List<ParkingAreaDto> parkingAreasDto = mapStructMapper.parkingAreasToParkingAreasDto(parkingAreas);

        return parkingAreasDto;
    }

    public ParkingAreaDto getById(Long id) throws NotFoundException{
        Optional<ParkingArea> parkingArea = repository.findById(id);
        
        if(!parkingArea.isPresent()){
            throw new NotFoundException("Object id not found");
        }

        ParkingAreaDto parkingAreaDto = mapStructMapper.parkingAreaToParkingAreaDto(parkingArea.get());

        return parkingAreaDto;
    }

    public ParkingAreaWithParkingSpotsDto getByIdWithParkingSpot(Long id){
        getById(id);

        ParkingArea parkingArea = repository.findById(id).get();

        ParkingAreaWithParkingSpotsDto parkingAreaWithParkingSpotDto = mapStructMapper
            .parkingAreaToParkingAreaWithParkingSpotsDto(parkingArea);

        return parkingAreaWithParkingSpotDto;
    }

    public ParkingAreaDto create(ParkingAreaDto parkingAreaDto){
        ParkingArea parkingArea = mapStructMapper.parkingAreaDtoToParkingArea(parkingAreaDto);

        ParkingArea parkingAreaSaved = repository.save(parkingArea);
        ParkingAreaDto parkingAreaDtoSaved = mapStructMapper.parkingAreaToParkingAreaDto(parkingAreaSaved);

        return parkingAreaDtoSaved;
    }

    public ParkingAreaDto update(ParkingAreaDto parkingAreaDto) throws NotFoundException{
        Long id = parkingAreaDto.getId();

        getById(id);

        ParkingArea parkingArea = mapStructMapper.parkingAreaDtoToParkingArea(parkingAreaDto);
        ParkingArea parkingAreaSaved = repository.save(parkingArea);
        ParkingAreaDto parkingAreaDtoSaved = mapStructMapper.parkingAreaToParkingAreaDto(parkingAreaSaved);

        return parkingAreaDtoSaved;
    }

    public void delete(Long id) throws NotFoundException {
        getById(id);

        repository.deleteById(id);
    }
}
