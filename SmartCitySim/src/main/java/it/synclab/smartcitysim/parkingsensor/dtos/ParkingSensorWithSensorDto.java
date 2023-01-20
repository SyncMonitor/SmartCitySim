package it.synclab.smartcitysim.parkingsensor.dtos;

import java.util.Date;

import it.synclab.smartcitysim.sensor.entities.Sensor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingSensorWithSensorDto {
    private Long id;

    private String address;

    private String latitude;

    private String longitude;

    private Boolean value;

    private Date timestamp;

    private Sensor sensor;
}
