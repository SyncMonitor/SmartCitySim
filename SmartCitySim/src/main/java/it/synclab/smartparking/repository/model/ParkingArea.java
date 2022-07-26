package it.synclab.smartparking.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "parking_area", uniqueConstraints = {
		@UniqueConstraint(name = "UniqueLatitudeAndLongitude", columnNames = { "latitude", "longitude" }) })
public class ParkingArea {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "fk_sensor_id")
	private Long fkSensorId;
	private String latitude;
	private String longitude;
	private String address;
	private boolean value;

	public ParkingArea() {
	}

	public ParkingArea(String latitude, String longitude, String address, boolean value) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSensorId() {
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

	@Override
	public String toString() {
		return "ParkingArea [Id = " + id + "sensorId = " + fkSensorId + ", latitude = " + latitude + ", longitude = "
				+ longitude + ", address = " + address + ", value = " + value + "]";
	}
}
