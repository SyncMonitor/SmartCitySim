package it.synclab.smartparking.batch;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;

import it.synclab.smartparking.repository.model.Pm25;

import lombok.Data;

@Data
public class Pm25Reader implements ItemReader<Pm25>
{
	private int nextDataIndex;
    private List<SensorsData> sensorsDatas;
    private List<Pm25> pm25Data;
    
    //Set initial data that does not change manually
    @Value("${sensor.environmental.address}")
    private String address;
    @Value("${sensor.environmental.latitude}")
  	private String latitude;
  	@Value("${sensor.environmental.longitude}")
  	private String longitude;
  	private Long fkSensorId = 16L;
    
    public Pm25Reader(List<SensorsData> sensorsDatas, List<Pm25> pm25Data)
    {
    	this.sensorsDatas = sensorsDatas;
    	this.pm25Data = pm25Data;
    }

	@Override
	public Pm25 read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		Pm25 nextPm25Data = null;
		
		while (nextDataIndex < sensorsDatas.size() && nextPm25Data == null) 
		{
			SensorsData nextSensorsData = sensorsDatas.get(nextDataIndex);
			
			String timestamp = nextSensorsData.getDate() + "-" + nextSensorsData.getTime();
			String value = nextSensorsData.getPm25Values();
			//pm2.5 sensor is number 16
			nextPm25Data = new Pm25(timestamp,address,latitude,longitude,value,fkSensorId);
			
			if(!pm25Data.contains(nextPm25Data))
			{
				pm25Data.add(nextPm25Data);
				nextDataIndex++;
				return nextPm25Data;
			}
			else
			{
				nextDataIndex++;
				nextPm25Data = null;
			}
        }
		
        nextDataIndex = 0;		
		return nextPm25Data;
	}

}
