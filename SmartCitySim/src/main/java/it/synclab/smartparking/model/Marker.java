package it.synclab.smartparking.model;

public class Marker {
	private int id;
	private String name;
	private String address;
	private double lat;
	private double lng;
	private int state;
	private String battery;
	private int active;

	@Override
	public String toString() {
		return "\n\t\t\t{\n\t\t\t\t\"id\" : " + id 
	+ ",\n\t\t\t\t \"name\" : \"" + name + "\"" 
	+ ",\n\t\t\t\t \"address\" : \"" + address + "\"" 
	+ ",\n\t\t\t\t \"lat\" : " + lat 
	+ ",\n\t\t\t\t \"lng\" : " + lng
	+ ",\n\t\t\t\t \"state\" : " + state 
	+ ",\n\t\t\t\t \"battery\" : \"" + battery + "\"" 
	+ ",\n\t\t\t\t \"active\" : " + active 
	+ "\n\t\t\t}";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getBattery() {
		return battery;
	}

	public void setBattery(String battery) {
		this.battery = battery;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

}
