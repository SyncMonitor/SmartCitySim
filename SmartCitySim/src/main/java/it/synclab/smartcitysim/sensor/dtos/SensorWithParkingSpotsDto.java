package it.synclab.smartcitysim.sensor.dtos;

import java.util.Date;
import java.util.List;

import it.synclab.smartcitysim.parkingspot.entities.ParkingSpot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorWithParkingSpotsDto {
    private Long id;

    private String name;

    private String battery;

    private String charge;

    private String type;

    private Boolean isActive;

    private Date lastSurvey;

    private List<ParkingSpot> parkingSpots;
}
