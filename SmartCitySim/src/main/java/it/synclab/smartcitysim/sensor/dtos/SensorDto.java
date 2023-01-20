package it.synclab.smartcitysim.sensor.dtos;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorDto {
    private Long id;

    private String name;

    private String battery;

    private String charge;

    private String type;

    private Boolean isActive;

    private Date lastSurvey;
}
