package it.synclab.smartparking.repository.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Sensors {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	String name;
	String battery;
	String type;
	boolean isActive;

	public Sensors( String name, String battery, String type, boolean isActive) {
		this.name = name;
		this.battery = battery;
		this.type = type;
		this.isActive = isActive;
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
				+ isActive + "]";
	}
	
	

}
