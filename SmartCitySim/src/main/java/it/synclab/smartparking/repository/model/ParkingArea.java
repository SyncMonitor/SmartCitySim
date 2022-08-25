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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "parking_area", uniqueConstraints = {
		@UniqueConstraint(name = "UniqueFkSensorId", columnNames = { "fk_sensor_id" }) })
public class ParkingArea {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "fk_sensor_id")
	private Long fkSensorId;
	private String latitude;
	private String longitude;
	private String address;
	private boolean value;
	
	@Column(name = "last_update")
	private LocalDateTime lastUpdate;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_sensor_id", referencedColumnName = "id")	//references ParkingArea id
	private List<ParkingAreaStats> stats;

	public ParkingArea() {
	}

	public ParkingArea(String latitude, String longitude, String address, boolean value, List<ParkingAreaStats> stats) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.value = value;
		this.lastUpdate = LocalDateTime.now();
		this.stats = stats;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFkSensorId() {
		return fkSensorId;
	}

	public void setSensorId(Long sensorId) {
		this.fkSensorId = sensorId;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public LocalDateTime getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(LocalDateTime date) {
		this.lastUpdate = date;
	}
	
	public List<ParkingAreaStats> getStats() {
		return stats;
	}

	public void setStats(List<ParkingAreaStats> stats) {
		this.stats = stats;
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
	
}