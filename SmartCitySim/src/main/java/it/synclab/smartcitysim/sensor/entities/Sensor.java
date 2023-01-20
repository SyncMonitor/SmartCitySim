package it.synclab.smartcitysim.sensor.entities;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import it.synclab.smartcitysim.maintainerregistry.entities.MaintainerRegistry;
import it.synclab.smartcitysim.parkingsensor.entities.ParkingSensor;
import it.synclab.smartcitysim.parkingspot.entities.ParkingSpot;
import it.synclab.smartcitysim.sensormaintenance.entities.SensorMaintenance;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sensors")
public class Sensor implements it.synclab.smartcitysim.interfaces.Entity<Long> {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String battery;

    @Column(nullable = false)
    private String charge;

    @Column(nullable = false)
    private String type;

    @Column(
        name = "is_active",
        nullable = false
    )
    private Boolean isActive;

    // TODO: verify if it is inserted also in creation
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastSurvey;

    public Sensor(){}

    public Sensor(Long id, String name, String battery, String charge, String type, Boolean isActive){
        this.id = id;
        this.name = name;
        this.battery = battery;
        this.charge = charge;
        this.type = type;
        this.isActive = isActive;
    }

    @ManyToOne
    @JoinColumn(name = "fk_maintainer_id", referencedColumnName = "id")
    private MaintainerRegistry maintainer;

    @OneToOne(mappedBy = "sensor", fetch = FetchType.LAZY)
    @Getter
    @Setter
    @JsonManagedReference
    private SensorMaintenance sensorMaintenance;

    @ManyToMany
    @JoinTable()
    private List<ParkingSpot> parkingSpots;

    @OneToOne(mappedBy = "sensor")
    private ParkingSensor parkingSensor;
}
