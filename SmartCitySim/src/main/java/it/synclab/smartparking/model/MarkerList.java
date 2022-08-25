package it.synclab.smartparking.model;

public class MarkerList {

	private Markers markers;

	public MarkerList(Markers markers) {
		this.markers = markers;
	}

	public MarkerList() {
	}

	@Override
	public String toString() {
		return "{\n\t\"markers\" : {\n\t\t" + markers + "\n}";
	}

	public Markers getMarkers() {
		return markers;
	}

	public void setMarkers(Markers markers) {
		this.markers = markers;
	}
}
