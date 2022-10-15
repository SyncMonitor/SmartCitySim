package it.synclab.smartparking.repository.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "temperature")
@NoArgsConstructor
@AllArgsConstructor
public class Temperature 
{
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
	
	public Temperature(String timestamp, String address, String latitude, String longitude, String value, Long fkSensorId)
	{
		this.timestamp = timestamp;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.value = value;
		this.fkSensorId = fkSensorId;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		
		if(!(o instanceof Temperature))
			return false;
		
		Temperature tObject = (Temperature) o;
		
		return tObject.getTimestamp().compareTo(this.timestamp) == 0 &&
				tObject.getLatitude().compareTo(this.latitude) == 0 && 	
				tObject.getLongitude().compareTo(this.longitude) == 0 &&
				tObject.getAddress().compareTo(this.address) == 0 &&
				tObject.getValue().compareTo(this.value) == 0 &&
				tObject.getFkSensorId().compareTo(fkSensorId) == 0;
	}
}
