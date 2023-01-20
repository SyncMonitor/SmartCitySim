package it.synclab.smartcitysim.sensormaintenance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.synclab.smartcitysim.sensormaintenance.dtos.SensorMaintenanceDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/sensors/{id}/sensors-maintenance")
public class SensorMaintenanceSensorController {

    @Autowired
    private SensorMaintenanceService service;

    @PutMapping()
    @ResponseBody
    public SensorMaintenanceDto update(@PathVariable Long id, @Valid @RequestBody SensorMaintenanceDto sensorMaintenanceDto){
        return this.service.update(id, sensorMaintenanceDto);
    }
}
