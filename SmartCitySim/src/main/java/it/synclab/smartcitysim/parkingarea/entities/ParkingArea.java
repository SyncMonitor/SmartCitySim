package it.synclab.smartcitysim.parkingarea.entities;

import java.util.List;

import it.synclab.smartcitysim.parkingspot.entities.ParkingSpot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
    @Setter
@Entity
@Table(
    name = "parking_areas",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"latitude", "longitude"})}
)
public class ParkingArea implements it.synclab.smartcitysim.interfaces.Entity<Long> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    public ParkingArea(){}
    public ParkingArea(Long id, String address, String latitude, String longitude){
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @OneToMany(mappedBy = "parkingArea")
    private List<ParkingSpot> parkingSpots;
}
