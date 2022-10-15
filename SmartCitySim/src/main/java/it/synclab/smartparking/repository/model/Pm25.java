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
@Table(name = "particular_matter25")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pm25 
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
	
	public Pm25(String timestamp, String address, String latitude, String longitude, String value, Long fkSensorId)
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
		
		if(!(o instanceof Pm25))
			return false;
		
		Pm25 pm25object = (Pm25) o;
		
		return pm25object.getTimestamp().compareTo(this.timestamp) == 0 &&
				pm25object.getLatitude().compareTo(this.latitude) == 0 && 	
				pm25object.getLongitude().compareTo(this.longitude) == 0 &&
				pm25object.getAddress().compareTo(this.address) == 0 &&
				pm25object.getValue().compareTo(this.value) == 0 &&
				pm25object.getFkSensorId().compareTo(fkSensorId) == 0;
	}
}
