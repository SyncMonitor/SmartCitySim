package it.synclab.smartcitysim.sensormaintenance.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorMaintenanceDto {
    
    private Long id;

    private Boolean toBeRepaired;

    private Boolean toBeCharged;

    private Boolean isUpdating = false;
}
