package it.synclab.smartparking.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sensors_maintainer")
public class SensorsMaintainer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "fk_sensor_id")
	private Long fkSensorId;
	
	@Column(name = "name")
	private String ownerName;
	
	@Column(name = "surname")
	private String ownerSurname;
	
	private String company;
	
	@Column(name = "phone")
	private String phoneNumber;
	
	private String mail;
	
	@Column(name = "to_be_repaired")
	private boolean toBeRepaired;

	public SensorsMaintainer() {
		
	}

	public SensorsMaintainer(String ownerName, String ownerSurame, String company, String phoneNumber, String mail, boolean toBeRepaired) {
		this.ownerName = ownerName;
		this.ownerSurname = ownerSurame;
		this.company = company;
		this.phoneNumber = phoneNumber;
		this.mail = mail;
		this.toBeRepaired = toBeRepaired;
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

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerSurame() {
		return ownerSurname;
	}

	public void setOwnerSurname(String ownerSurame) {
		this.ownerSurname = ownerSurame;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public boolean isToBeRepaired() {
		return toBeRepaired;
	}

	public void setToBeRepaired(boolean toBeRepaired) {
		this.toBeRepaired = toBeRepaired;
	}

	@Override
	public String toString() {
		return "SensorsManteiner [id=" + id + ", fkSensorId=" + fkSensorId + ", ownerName=" + ownerName
				+ ", ownerSurame=" + ownerSurname + ", company=" + company + ", phoneNumber=" + phoneNumber + ", mail="
				+ mail + ", toBeRepaired=" + toBeRepaired + "]";
	}
}
