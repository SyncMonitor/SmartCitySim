package it.synclab.smartparking.repository.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "parking_area_stats", uniqueConstraints = { @UniqueConstraint(columnNames = { "fk_sensor_id", "last_update" }) })
public class ParkingAreaStats {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "fk_sensor_id")
	private Long fkSensorId;
	
	@Column(name = "last_update")
	LocalDateTime lastUpdate;
	
	private boolean value;
	
	public ParkingAreaStats() {}
	
	public ParkingAreaStats(Long fkSensorId, LocalDateTime lastUpdate, boolean value) {
		this.fkSensorId = fkSensorId;
		this.lastUpdate = lastUpdate;
		this.value = value;
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

	public void setFkSensorId(Long fkSensorId) {
		this.fkSensorId = fkSensorId;
	}

	public LocalDateTime getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(LocalDateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ParkingAreaStats [id=" + id + ", fkSensorId=" + fkSensorId + ", lastUpdate=" + lastUpdate + ", value="
				+ value + "]";
	}
	
	
	
	
	
	
}
