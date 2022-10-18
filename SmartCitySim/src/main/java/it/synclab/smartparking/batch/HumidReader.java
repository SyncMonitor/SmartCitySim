package it.synclab.smartparking.batch;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;

import it.synclab.smartparking.repository.model.Humidity;

import lombok.Data;

@Data
public class HumidReader implements ItemReader<Humidity>
{
	private int nextDataIndex;
    private List<SensorsData> sensorsDatas;
    private List<Humidity> humidData;
    
    //Set initial data that does not change
    @Value("${sensor.environmental.address}")
    private String address;
    @Value("${sensor.environmental.latitude}")
  	private String latitude;
  	@Value("${sensor.environmental.longitude}")
  	private String longitude;
  	private Long fkSensorId = 19L;
    
    public HumidReader(List<SensorsData> sensorsDatas, List<Humidity> humidData)
    {
    	this.sensorsDatas = sensorsDatas;
    	this.humidData = humidData;
    }

	@Override
	public Humidity read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		Humidity nextHumidData = null;
		
		while (nextDataIndex < sensorsDatas.size() && nextHumidData == null) 
		{
			SensorsData nextSensorsData = sensorsDatas.get(nextDataIndex);
			
			String timestamp = nextSensorsData.getDate() + "-" + nextSensorsData.getTime();
			String value = nextSensorsData.getTempValues();
			//humidity sensor is number 19
			nextHumidData = new Humidity(timestamp,address,latitude,longitude,value,fkSensorId);
			
			if(!humidData.contains(nextHumidData))
			{
				humidData.add(nextHumidData);
				nextDataIndex++;
				return nextHumidData;
			}
			else
			{
				nextDataIndex++;
				nextHumidData = null;
			}
        }
		
        nextDataIndex = 0;
		return nextHumidData;
	}

}
