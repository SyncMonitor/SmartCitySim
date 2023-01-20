package it.synclab.smartcitysim.sensor.dtos;

import java.util.Date;

import it.synclab.smartcitysim.parkingsensor.entities.ParkingSensor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorWithParkingSensorDto {
    private Long id;

    private String name;

    private String battery;

    private String charge;

    private String type;

    private Boolean isActive;

    private Date lastSurvey;

    private ParkingSensor parkingSensor;
}
