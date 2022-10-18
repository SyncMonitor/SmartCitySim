package it.synclab.smartparking.batch;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;

import it.synclab.smartparking.repository.model.Pm10;

import lombok.Data;

@Data
public class Pm10Reader implements ItemReader<Pm10>
{
	private int nextDataIndex;
    private List<SensorsData> sensorsDatas;
    private List<Pm10> pm10Data;
    
    //Set initial data that does not change
    @Value("${sensor.environmental.address}")
    private String address;
    @Value("${sensor.environmental.latitude}")
  	private String latitude;
  	@Value("${sensor.environmental.longitude}")
  	private String longitude;
  	private Long fkSensorId = 17L;
    
    public Pm10Reader(List<SensorsData> sensorsDatas, List<Pm10> pm10Data)
    {
    	this.sensorsDatas = sensorsDatas;
    	this.pm10Data = pm10Data;
    }

	@Override
	public Pm10 read() 
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException 
	{
		Pm10 nextPm10Data = null;		
		
		while (nextDataIndex < sensorsDatas.size() && nextPm10Data == null) {
			
			SensorsData nextSensorsData = sensorsDatas.get(nextDataIndex);
			
			String timestamp = nextSensorsData.getDate() + "-" + nextSensorsData.getTime();
			String value = nextSensorsData.getTempValues();
			//pm2.5 sensor is number 16
			nextPm10Data = new Pm10(timestamp,address,latitude,longitude,value,fkSensorId);
			if(!pm10Data.contains(nextPm10Data))
			{
				pm10Data.add(nextPm10Data);
				nextDataIndex++;
				return nextPm10Data;
			}
			else
			{
				nextDataIndex++;
				nextPm10Data = null;
			}
        }
        	
		nextDataIndex = 0;
		return nextPm10Data;
	}

}
