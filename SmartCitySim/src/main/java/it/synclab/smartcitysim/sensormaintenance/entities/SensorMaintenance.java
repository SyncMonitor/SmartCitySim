package it.synclab.smartcitysim.sensormaintenance.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import it.synclab.smartcitysim.sensor.entities.Sensor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sensors_maintenance")
public class SensorMaintenance implements it.synclab.smartcitysim.interfaces.Entity<Long> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(
        name = "to_be_repaired",
        nullable = false
    )
    private Boolean toBeRepaired = false;

    @Column(
        name = "to_be_charged",
        nullable = false
    )
    private Boolean toBeCharged = false;

    @Column(
        name = "is_updating",
        nullable = false
    )
    private Boolean isUpdating = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "fk_sensor_id", 
        referencedColumnName = "id",
        nullable = false
    )
    @JsonBackReference
    private Sensor sensor;
}
