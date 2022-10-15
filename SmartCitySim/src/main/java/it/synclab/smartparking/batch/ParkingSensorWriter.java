package it.synclab.smartparking.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import it.synclab.smartparking.repository.model.ParkingSensors;
import it.synclab.smartparking.repository.ParkingSensorsRepository;

public class ParkingSensorWriter implements ItemWriter<ParkingSensors>
{ 	
	ParkingSensorsRepository parkingSensorsRepository;
	
	@Override
	public void write(List<? extends ParkingSensors> items) throws Exception 
	{
		parkingSensorsRepository.saveAll(items);
	}
	
	public void setRepository(ParkingSensorsRepository parkingSensorsRepository)
	{
		this.parkingSensorsRepository = parkingSensorsRepository;
	}
}