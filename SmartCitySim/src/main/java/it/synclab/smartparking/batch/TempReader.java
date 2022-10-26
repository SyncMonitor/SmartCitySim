package it.synclab.smartparking.batch;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;

import it.synclab.smartparking.repository.model.Temperature;

import lombok.Data;

@Data
public class TempReader implements ItemReader<Temperature>
{
	private int nextDataIndex;
    private List<SensorsData> sensorsDatas;
    private List<Temperature> tempData;
    
    //Set initial data that does not change
    @Value("${sensor.environmental.address}")
    private String address;
    @Value("${sensor.environmental.latitude}")
  	private String latitude;
  	@Value("${sensor.environmental.longitude}")
  	private String longitude;
  	private Long fkSensorId = 18L;
    
    public TempReader(List<SensorsData> sensorsDatas, List<Temperature> tempData)
    {
    	this.sensorsDatas = sensorsDatas;
    	this.tempData = tempData;
    }

	@Override
	public Temperature read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		
		Temperature nextTempData = null;
		
		while (nextDataIndex < sensorsDatas.size() && nextTempData == null) 
		{
			SensorsData nextSensorsData = sensorsDatas.get(nextDataIndex);
			
			String timestamp = nextSensorsData.getDate() + "-" + nextSensorsData.getTime();
			String value = nextSensorsData.getTempValues();
			//pm2.5 sensor is number 16
			nextTempData = new Temperature(timestamp,address,latitude,longitude,value,fkSensorId);

			if(!tempData.contains(nextTempData))
			{
				tempData.add(nextTempData);
				nextDataIndex++;
				return nextTempData;
			}
			else
			{
				nextDataIndex++;
				nextTempData = null;
			}
        }
        nextDataIndex = 0;
		
		return nextTempData;
	}

}
