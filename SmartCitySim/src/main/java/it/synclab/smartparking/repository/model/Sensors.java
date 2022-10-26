package it.synclab.smartparking.repository.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sensors")
@Data
@NoArgsConstructor
public class Sensors {

	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	String name;
	String battery;
	String charge;
	String type;
	boolean isActive;
	LocalDateTime lastSurvey;

	@OneToOne(mappedBy = "sensors")
	private ParkingSensors parkingSensors;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "fkSensorId", orphanRemoval = true)
	private List<Pm10> pm10;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "fkSensorId", orphanRemoval = true)
	private List<Pm25> pm25;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "fkSensorId", orphanRemoval = true)
	private List<Temperature> temperature;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "fkSensorId", orphanRemoval = true)
	private List<Humidity> humidity;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_sensor_id", referencedColumnName = "id")
	private List<MaintainersRegistry> maintainers;
	
	@ManyToMany(mappedBy = "sensors")
	private List<ParkingSpots> parkingSpots;
	
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
				+ "\t\"ParkingArea\":" + parkingSensors.toString() + "\n}";
	}

	public Sensors(long id, String name, String battery, String charge, String type, boolean isActive,
			ParkingSensors parkingSensor) {
		this.id = id;
		this.name = name;
		this.battery = battery;
		this.charge = charge;
		this.type = type;
		this.isActive = isActive;
		this.parkingSensors = parkingSensor;
	}
	

}
