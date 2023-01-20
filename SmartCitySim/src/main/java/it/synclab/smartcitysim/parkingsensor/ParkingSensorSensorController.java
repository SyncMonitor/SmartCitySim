package it.synclab.smartcitysim.parkingsensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.synclab.smartcitysim.parkingsensor.dtos.ParkingSensorDto;

@RestController
@RequestMapping("/sensors/{id}/parking-sensors")
public class ParkingSensorSensorController {
    
    @Autowired
    private ParkingSensorService service;

    @GetMapping()
    @ResponseBody
    public ParkingSensorDto getBySensorId(@PathVariable Long id){
        return service.getBySensorId(id);
    }
}
