package it.synclab.smartcitysim.parkingspot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.synclab.smartcitysim.parkingspot.dtos.ParkingSpotDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/parking-areas/{id}/parking-spots")
public class ParkingSpotParkingAreaController{

    @Autowired
    protected ParkingSpotService service;

    @GetMapping()
    @ResponseBody
    public List<ParkingSpotDto> getAll(@PathVariable Long id){
        return service.getAllByParkingAreaId(id);
    }

    @PostMapping()
    @ResponseBody
    public ParkingSpotDto create(@PathVariable Long id, @Valid @RequestBody ParkingSpotDto parkingSpotDto){
        return this.service.create(id, parkingSpotDto);
    }
}
