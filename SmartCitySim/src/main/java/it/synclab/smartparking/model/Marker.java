package it.synclab.smartparking.model;

public class Marker {
	private Long id;
	private String name;
	private String address;
	private String lat;
	private String lng;
	private boolean state;
	private String battery;
	private boolean active;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getBattery() {
		return battery;
	}

	public void setBattery(String battery) {
		this.battery = battery;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
