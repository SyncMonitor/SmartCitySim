package it.synclab.smartcitysim.parkingspot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.synclab.smartcitysim.parkingspot.dtos.ParkingSpotDto;

@RestController
@RequestMapping("/sensors/{id}/parking-spots")
public class ParkingSpotSensorController {
    
    @Autowired
    private ParkingSpotService service;

    @GetMapping()
    @ResponseBody
    public List<ParkingSpotDto> getAllBySensorId(@PathVariable Long id){
        return service.getAllBySensorId(id);
    }
}
