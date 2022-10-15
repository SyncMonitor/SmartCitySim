package it.synclab.smartparking.repository.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import it.synclab.smartparking.batch.ParkingSensorsInputData;

import lombok.Data;

@Entity
@Table(name = "parking_sensors")
@Data
public class ParkingSensors {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne
	@JoinColumn(name = "fk_sensor_id", referencedColumnName = "id")
	private Sensors sensors;
	private String latitude;
	private String longitude;
	private String address;
	private boolean value;
	
	@Column(name = "timestamp")
	private LocalDateTime timestamp;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_sensor_id", referencedColumnName = "id")	//references ParkingArea id
	private List<ParkingAreaStats> stats;

	public ParkingSensors() {
	}

	public ParkingSensors(String latitude, String longitude, String address, boolean value, List<ParkingAreaStats> stats) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.value = value;
		this.timestamp = LocalDateTime.now();
		this.stats = stats;
	}

	public Long getFkSensorId() {
		return sensors.getId();
	}
	
	public boolean getValue()
	{
		return value;
	}

	@Override
	public String toString() {
		return "\n\t\t{\n"
//				+ "\t\t\"id\" : " + id + ",\n" 
//				+ "\t\t\"sensorId\" : " + fkSensorId + ",\n" 
				+ "\t\t\"latitude\" : \"" + latitude + "\",\n"
				+ "\t\t\"longitude\" : \"" + longitude + "\",\n"
				+ "\t\t\"address\":\"" + address + "\",\n"
				+ "\t\t\"value\" : " + value + "\n\t\t}\n\t";
	}
	
	public ParkingSensors(ParkingSensorsInputData p)
	{
		this.address = p.getAddress();
		this.latitude = p.getLatitude();
		this.longitude = p.getLongitude();
		this.value = p.isValue();
		this.timestamp = LocalDateTime.now();
	}
	
}