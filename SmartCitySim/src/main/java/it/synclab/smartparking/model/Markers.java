package it.synclab.smartparking.model;

import java.util.ArrayList;
import java.util.List;

public class Markers {
	public List<Marker> markers = new ArrayList<Marker>();

	public Markers() {
	}

	public Markers(List<Marker> markers) {
		this.markers = markers;
	}

	public List<Marker> getMarkers() {
		return markers;
	}

	public void setMarker(List<Marker> markers) {
		this.markers = markers;
	}

	@Override
	public String toString() {
		return "\"marker\" : " + markers + "\n\t}";
	}

}
