package it.synclab.smartparking.repository.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Entity
@Table(name = "parking_area", 
		uniqueConstraints = { @UniqueConstraint(name = "UniqueCoordinates", columnNames = { "latitude", "longitude" }) })
@Data
public class ParkingArea 
{
	@Id
	private Long id;
	private String address;
	private String latitude;
	private String longitude;
	@OneToMany(mappedBy = "parkingArea")
	private List<ParkingSpots> parkingSpots; 
}
