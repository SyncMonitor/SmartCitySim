package it.synclab.smartparking.batch;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import it.synclab.smartparking.repository.model.ParkingSensors;
import it.synclab.smartparking.repository.ParkingSensorsRepository;

public class ParkingSensorsClearer implements ItemReader<ParkingSensors>
{	
	private ParkingSensorsRepository parkingSensorsRepository;
	
	@Override
	public ParkingSensors read() 
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException 
	{
		parkingSensorsRepository.deleteAll();
		return null;
	}
	
	public void setRepository(ParkingSensorsRepository parkingSensorsRepository)
	{
		this.parkingSensorsRepository = parkingSensorsRepository;
	}
}
