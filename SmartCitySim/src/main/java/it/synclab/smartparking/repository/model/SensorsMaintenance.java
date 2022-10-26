package it.synclab.smartparking.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "sensors_maintenance")
@Data
public class SensorsMaintenance {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "to_be_repaired")
	private boolean toBeRepaired;

	@Column(name = "to_be_charged")
	private boolean toBeCharged;

	@Column(name = "isUpdating")
	boolean isUpdating;
}
