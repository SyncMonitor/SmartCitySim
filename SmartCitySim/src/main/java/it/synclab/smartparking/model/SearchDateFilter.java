package it.synclab.smartparking.model;

public class SearchDateFilter {

	private String startDate;
	private String endDate;

	public SearchDateFilter(String startDate, String endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public SearchDateFilter() {
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String date) {
		this.startDate = date;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String date) {
		this.endDate = date;
	}

	@Override
	public String toString() {
		return "SearchDateFilter [startDate=" + startDate + ", endDate=" + endDate + "]";
	}

}