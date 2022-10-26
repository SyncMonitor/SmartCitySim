package it.synclab.smartparking.repository.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "float_sensors")
@Data
public class FloatSensors {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "fk_sensor_id")
	private Long fkSensorId;
	private String address;
	private String latitude;
	private String longitude;
	@Basic(optional = false)
	private String value;
	private String timestamp;	

}
