package it.synclab.smartparking.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "sensors_maintainer", uniqueConstraints = { @UniqueConstraint(columnNames = { "fk_sensor_id", "type" }) })
public class SensorsMaintainer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	private String type;
	
	@Column(name = "to_be_repaired")
	private boolean toBeRepaired;
	
	@Column(name = "to_be_charged")
	private boolean toBeCharged;

	public SensorsMaintainer() {
		
	}

	public SensorsMaintainer(String ownerName, String ownerSurame, String company, String phoneNumber, String mail, String type, boolean toBeRepaired, boolean toBeCharged) {
		this.ownerName = ownerName;
		this.ownerSurname = ownerSurame;
		this.company = company;
		this.phoneNumber = phoneNumber;
		this.mail = mail;
		this.type = type;
		this.toBeRepaired = toBeRepaired;
		this.toBeCharged = toBeCharged;
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

	public String getOwnerSurname() {
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
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public boolean isToBeRepaired() {
		return toBeRepaired;
	}

	public void setToBeRepaired(boolean toBeRepaired) {
		this.toBeRepaired = toBeRepaired;
	}

	public boolean isToBeCharged() {
		return toBeCharged;
	}

	public void setToBeCharged(boolean toBeCharged) {
		this.toBeCharged = toBeCharged;
	}

	@Override
	public String toString() {
		return "SensorsMaintainer [id=" + id + ", fkSensorId=" + fkSensorId + ", ownerName=" + ownerName
				+ ", ownerSurname=" + ownerSurname + ", company=" + company + ", phoneNumber=" + phoneNumber + ", mail="
				+ mail + ", type=" + type + ", toBeRepaired=" + toBeRepaired + ", toBeCharged=" + toBeCharged + "]";
	}
}
