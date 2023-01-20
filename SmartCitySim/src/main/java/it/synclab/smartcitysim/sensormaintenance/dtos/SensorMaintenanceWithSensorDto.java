package it.synclab.smartcitysim.sensormaintenance.dtos;

import it.synclab.smartcitysim.sensor.entities.Sensor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorMaintenanceWithSensorDto {
    private Long id;

    private Boolean toBeRepaired;

    private Boolean toBeCharged;

    private Boolean isUpdating = false;

    private Sensor sensor;
}
