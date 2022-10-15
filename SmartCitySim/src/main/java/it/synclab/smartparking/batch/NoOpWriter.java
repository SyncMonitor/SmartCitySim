package it.synclab.smartparking.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import it.synclab.smartparking.repository.model.ParkingSensors;

public class NoOpWriter implements ItemWriter<ParkingSensors>
{ 		
	@Override
	public void write(List<? extends ParkingSensors> items) throws Exception 
	{
		
	}
}