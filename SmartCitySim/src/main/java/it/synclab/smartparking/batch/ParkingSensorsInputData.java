package it.synclab.smartparking.batch;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlRootElement(name="marker")
public class ParkingSensorsInputData 
{
	@XmlAttribute(name = "id", required = true)
	private Long fkSensorId;
	private String address;
	private String latitude;
	private String longitude;
	private boolean value;
	private LocalDateTime timestamp;
	
	public Long getfkSensorId() {
		return fkSensorId;
	}
	
	@XmlAttribute(name = "address", required = true)
	public String getAddress() {
		return address;
	}
	
	@XmlAttribute(name = "lat", required = true)
	public String getLatitude() {
		return latitude;
	}
	
	@XmlAttribute(name = "lng", required = true)
	public String getLongitude() {
		return longitude;
	}
	
	@XmlAttribute(name = "state", required = true)
	public boolean isValue() {
		return value;
	}
	
	public ParkingSensorsInputData()
	{
		this.timestamp = LocalDateTime.now();
	}
}