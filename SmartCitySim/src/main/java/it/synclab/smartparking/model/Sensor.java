package it.synclab.smartparking.model;

public class Sensor {

	private int id;
	
	private int state;
	
	//Insert all informations about a sensor

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getId() {
		return id;
		
	}

	public void setId(int id) {
		this.id = id;
	}
}
