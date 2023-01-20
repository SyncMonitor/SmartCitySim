package it.synclab.smartcitysim.parkingsensor.dtos;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingSensorDto {

    private Long id;

    @NotBlank
    private String address;

    @NotBlank
    private String latitude;

    @NotBlank
    private String longitude;

    @NotBlank
    private Boolean value;

    @NotBlank
    private Date timestamp;
}
