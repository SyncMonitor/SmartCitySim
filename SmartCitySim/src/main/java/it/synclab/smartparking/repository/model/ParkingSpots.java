package it.synclab.smartparking.repository.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import lombok.Data;

@Entity
@Table(name="parking_spots")
@Data
public class ParkingSpots 
{
	@Id
	private Long id;
	private String latitude;
	private String longitude;
	
	@ManyToOne
	@JoinColumn(name="parking_area_id", nullable=false)
	private ParkingArea parkingArea;
	
	@ManyToMany
	@JoinTable(
			  name = "parking_spots_sensors", 
			  joinColumns = @JoinColumn(name = "fk_parking_spot_id"), 
			  inverseJoinColumns = @JoinColumn(name = "fk_sensor_id"))
	private List<Sensors> sensors;
}
