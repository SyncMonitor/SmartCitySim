package it.synclab.smartcitysim.parkingspot.entities;

import java.util.List;

import it.synclab.smartcitysim.parkingarea.entities.ParkingArea;
import it.synclab.smartcitysim.sensor.entities.Sensor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

// TODO: create an API to associate sensor to parking spot, and viceversa

@Getter
@Setter
@Entity
@Table(
    name = "parking_spots",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"latitude", "longitude"})}
)
public class ParkingSpot implements it.synclab.smartcitysim.interfaces.Entity<Long> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @ManyToOne
    @JoinColumn(
        name = "fk_parking_area_id", 
        referencedColumnName = "id",
        nullable = false
    )
    private ParkingArea parkingArea;

    @ManyToMany(mappedBy = "parkingSpots")
    private List<Sensor> sensors;
}
