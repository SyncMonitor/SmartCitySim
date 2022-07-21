package it.synclab.smartparking.repository.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "sensors")
public class Sensor {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	String name;
	String battery;
	String type;
	boolean isActive;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_sensor_id", referencedColumnName = "id")
	private List<ParkingArea> parkingArea;

	public Sensor() {
	};

	public Sensor(String name, String battery, String type, boolean isActive, List<ParkingArea> parkingArea) {
		this.name = name;
		this.battery = battery;
		this.type = type;
		this.isActive = isActive;
		this.parkingArea = parkingArea;
	}

	public List<ParkingArea> getParkingArea() {
		return parkingArea;
	}

	public void setParkingArea(List<ParkingArea> parkingArea) {
		this.parkingArea = parkingArea;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBattery() {
		return battery;
	}

	public void setBattery(String battery) {
		this.battery = battery;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "Sensors [id=" + id + ", name=" + name + ", battery=" + battery + ", type=" + type + ", isActive="
				+ isActive + parkingArea.toString() + "]";
	}

}
