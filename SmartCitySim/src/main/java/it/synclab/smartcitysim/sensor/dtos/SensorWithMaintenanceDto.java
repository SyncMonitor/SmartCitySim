package it.synclab.smartcitysim.sensor.dtos;

import java.util.Date;

import it.synclab.smartcitysim.sensormaintenance.entities.SensorMaintenance;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorWithMaintenanceDto {
    private Long id;

    private String name;

    private String battery;

    private String charge;

    private String type;

    private Boolean isActive;

    private Date lastSurvey;

    private SensorMaintenance sensorMaintenance;
}
