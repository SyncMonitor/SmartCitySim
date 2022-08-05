package it.synclab.smartparking.model;

import java.util.ArrayList;

public class Markers { 
	public ArrayList<Marker> markers;
	
	@Override
	public String toString() {
		return "\"marker\" : " + markers + "\n\t}";
	}


	public ArrayList<Marker> getMarkers() {
		return markers;
	}

	public void setMarker(ArrayList<Marker> markers) {
		this.markers = markers;
	}
	
}
