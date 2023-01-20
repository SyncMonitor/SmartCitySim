package it.synclab.smartcitysim.parkingspot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.synclab.smartcitysim.exception.NoHandlerFoundException;
import it.synclab.smartcitysim.parkingspot.dtos.ParkingSpotDto;
import it.synclab.smartcitysim.sensor.dtos.SensorDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/parking-spots")
public class ParkingSpotController{

    @Autowired
    protected ParkingSpotService service;

    @GetMapping("/{id}/sensors")
    @ResponseBody
    public List<SensorDto> getAllSensorsById(@PathVariable Long id){
        return this.service.getAllSensorsById(id);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ParkingSpotDto update(@PathVariable Long id, @Valid @RequestBody ParkingSpotDto parkingSpotDto) throws NoHandlerFoundException {

        parkingSpotDto.setId(id);

        return this.service.update(parkingSpotDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws NoHandlerFoundException{
        this.service.delete(id);
    }
}
