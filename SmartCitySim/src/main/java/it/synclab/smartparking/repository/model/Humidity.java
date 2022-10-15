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
@Table(name = "humidity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Humidity 
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
	
	public Humidity(String timestamp, String address, String latitude, String longitude, String value, Long fkSensorId)
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
		
		if(!(o instanceof Humidity))
			return false;
		
		Humidity hObject = (Humidity) o;
		
		return hObject.getTimestamp().compareTo(this.timestamp) == 0 &&
				hObject.getLatitude().compareTo(this.latitude) == 0 && 	
				hObject.getLongitude().compareTo(this.longitude) == 0 &&
				hObject.getAddress().compareTo(this.address) == 0 &&
				hObject.getValue().compareTo(this.value) == 0 &&
				hObject.getFkSensorId().compareTo(fkSensorId) == 0;
	}
}
