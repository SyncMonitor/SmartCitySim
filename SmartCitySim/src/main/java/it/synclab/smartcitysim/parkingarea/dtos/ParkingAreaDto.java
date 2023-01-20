package it.synclab.smartcitysim.parkingarea.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingAreaDto {
    
    private Long id;

    @NotBlank
    private String address;

    @NotBlank
    @Pattern(regexp="^[0-9]+\\.?[0-9]+$",message = "must be a numeric string")
    private String latitude;

    @NotBlank
    @Pattern(regexp="^[0-9]+\\.?[0-9]+$",message = "must be a numeric string")
    private String longitude;
}
