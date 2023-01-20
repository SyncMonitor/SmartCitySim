package it.synclab.smartcitysim.parkingarea.dtos;

import java.util.List;

import it.synclab.smartcitysim.parkingspot.entities.ParkingSpot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingAreaWithParkingSpotsDto {
    private Long id;

    private String address;

    private String latitude;

    private String longitude;

    private List<ParkingSpot> parkingSpots;
}
