package it.synclab.smartparking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import it.synclab.smartparking.model.Sensor;

@Service
public class ParkingService {

	public List<Sensor> getSensorsState() {
		//Read data from database
		return null;
	}
	
	/*
	 * 0 free
	 * 1 occupy
	 * **/
	public int getSensorState(String sensorId) {
		//Read data from database
		return 0;
	}
	
}
