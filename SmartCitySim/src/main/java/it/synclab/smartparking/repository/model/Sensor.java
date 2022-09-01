package it.synclab.smartparking.repository.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "sensors")
public class Sensor {

	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	String name;
	String battery;
	String charge;
	String type;
	boolean isActive;
	LocalDateTime lastSurvey;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_sensor_id", referencedColumnName = "id")
	private List<ParkingArea> parkingArea;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_sensor_id", referencedColumnName = "id")
	private List<SensorsMaintainer> maintainer;

	public Sensor() {
	}

	public Sensor(Long id, String name, String battery, String charge, String type, boolean isActive, List<ParkingArea> parkingArea) {
		this.id = id;
		this.name = name;
		this.battery = battery;
		this.charge = charge;
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
	
	public void setMaintainers(List<SensorsMaintainer> maintainers) {
		this.maintainer = maintainers;
	}

	public List<SensorsMaintainer> getMaintainer() {
		return this.maintainer;
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
	
	public String getCharge() {
		return charge;
	}

	public void setCharge(String c) {
		this.charge = c;
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

	public LocalDateTime getLastSurvey() {
		return lastSurvey;
	}

	public void setLastSurvey(LocalDateTime lastSurvey) {
		this.lastSurvey = lastSurvey;
	}
	
	@Override
	public String toString() {
		return "\n{\n"
				+ "\t\"id\" : " + id + ",\n" 
				+ "\t\"name\" : " + name + ",\n" 
				+ "\t\"battery\" : \"" + battery + "\",\n"
				+ "\t\"charge\" : \"" + charge + "\",\n"
				+ "\t\"type\" : \"" + type + "\",\n"
				+ "\t\"isActive\":\"" + isActive + "\",\n"
				+ "\t\"lastSurvey\":\"" + lastSurvey + "\",\n"
				+ "\t\"ParkingArea\":" + parkingArea.toString() + "\n}";
	}
	

}
