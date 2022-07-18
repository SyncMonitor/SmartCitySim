package it.synclab.smartparking.model;

import java.util.ArrayList;

public class Markers { 
	public ArrayList<Marker> marker;
	
	@Override
	public String toString() {
		return "\"marker\" : " + marker + "\n\t}";
	}


	public ArrayList<Marker> getMarker() {
		return marker;
	}

	public void setMarker(ArrayList<Marker> marker) {
		this.marker = marker;
	}
	
}
