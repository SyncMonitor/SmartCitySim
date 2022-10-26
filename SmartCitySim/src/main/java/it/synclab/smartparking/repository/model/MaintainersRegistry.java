package it.synclab.smartparking.repository.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "maintainers_registry")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintainersRegistry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "fk_sensor_id")
	private Long fkSensorId;

	@Column(name = "name")
	private String ownerName;

	@Column(name = "surname")
	private String ownerSurname;

	@Column(unique = true)
	private String company;

	@Column(name = "phone", unique = true)
	private String phoneNumber;
	
	@Column(unique = true)
	private String mail;
	
	private String type;
	
	//private Sensors sensors;
	
	@Column(name = "to_be_repaired")
	private boolean toBeRepaired;

	@Column(name = "to_be_charged")
	private boolean toBeCharged;

	@Column(name = "isUpdating")
	private boolean isUpdating;
}
