package it.synclab.smartparking.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import lombok.Data;

@Data
public class SensorsDatasListWriter implements ItemWriter<SensorsData>
{ 	
	List<SensorsData> sensorsDatas;
	
	public SensorsDatasListWriter(List<SensorsData> sensorsDatas)
	{
		this.sensorsDatas = sensorsDatas;
	}
	
	@Override
	public void write(List<? extends SensorsData> items) throws Exception 
	{
		for(SensorsData item : items)
		{
			if(!sensorsDatas.contains(item))
				sensorsDatas.add(item);
		}	
	}
}