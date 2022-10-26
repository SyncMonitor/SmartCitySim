package it.synclab.smartparking.batch;

import java.time.LocalDateTime;

import org.springframework.batch.item.ItemProcessor;

import it.synclab.smartparking.repository.model.ParkingSensors;
import it.synclab.smartparking.repository.SensorsRepository;

public class ParkingSensorsProcessor implements ItemProcessor<ParkingSensorsInputData, ParkingSensors> 
{
	private SensorsRepository sensorsRepository;
	
	@Override
	public ParkingSensors process(ParkingSensorsInputData parkingSensorsInputData) 
		  throws Exception 
	{		
		ParkingSensors parkingSensors = new ParkingSensors(parkingSensorsInputData);
	 	parkingSensors.setSensors(sensorsRepository.getSensorById(parkingSensorsInputData.getfkSensorId()));
	 	sensorsRepository.updateLastSurveyById(LocalDateTime.now(), parkingSensorsInputData.getfkSensorId());
	 	
	  	return parkingSensors;
	}
	  	
	public void setRepository(SensorsRepository sensorsRepository)
	{
		this.sensorsRepository = sensorsRepository;
	}
}
