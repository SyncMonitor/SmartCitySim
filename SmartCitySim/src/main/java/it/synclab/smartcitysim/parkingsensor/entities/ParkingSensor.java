package it.synclab.smartcitysim.parkingsensor.entities;

import java.util.Date;

import org.hibernate.annotations.UpdateTimestamp;

import it.synclab.smartcitysim.sensor.entities.Sensor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "parking_sensors")
public class ParkingSensor implements it.synclab.smartcitysim.interfaces.Entity<Long> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private Boolean value;

    @Column(nullable = false)
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @OneToOne
    @JoinColumn(
        name = "fk_sensor_id",
        referencedColumnName = "id",
        nullable = false
    )
    private Sensor sensor;
}
