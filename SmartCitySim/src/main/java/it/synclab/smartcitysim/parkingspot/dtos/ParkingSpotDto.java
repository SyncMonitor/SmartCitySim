package it.synclab.smartcitysim.parkingspot.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingSpotDto {

    private Long id;

    @NotBlank
    @Pattern(regexp="^[0-9]+\\.?[0-9]+$",message = "must be a numeric string")
    private String latitude;

    @NotBlank
    @Pattern(regexp="^[0-9]+\\.?[0-9]+$",message = "must be a numeric string")
    private String longitude;
}
