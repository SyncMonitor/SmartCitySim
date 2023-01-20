package it.synclab.smartcitysim.sensor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.synclab.smartcitysim.exception.NoHandlerFoundException;
import it.synclab.smartcitysim.sensor.dtos.SensorDto;
import it.synclab.smartcitysim.sensor.dtos.SensorWithMaintenanceDto;

@RestController
@RequestMapping("/sensors")
public class SensorController{

    @Autowired
    protected SensorService service;

    @GetMapping()
    @ResponseBody
    public List<SensorDto> getAll() throws NoHandlerFoundException {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public SensorDto getById(@PathVariable Long id) throws NoHandlerFoundException {
        return service.getById(id);
    }

    @GetMapping("/sensors-maintenance")
    @ResponseBody
    public List<SensorWithMaintenanceDto> getAllWithSensorMaintenance(){
        return this.service.getAllWithSensorMaintenance();
    }

    @GetMapping("/{id}/sensors-maintenance")
    @ResponseBody
    public SensorWithMaintenanceDto getByIdWithSensorMaintenance(@PathVariable Long id){
        return this.service.getByIdWithSensorMaintenance(id);
    }
}
