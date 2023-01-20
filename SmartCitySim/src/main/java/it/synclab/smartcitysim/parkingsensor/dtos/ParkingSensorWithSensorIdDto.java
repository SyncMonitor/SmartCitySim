package it.synclab.smartcitysim.parkingsensor.dtos;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingSensorWithSensorIdDto {
    private Long id;

    private String address;

    private String latitude;

    private String longitude;

    private Boolean value;

    private Date timestamp;

    private Long sensorId;
}
