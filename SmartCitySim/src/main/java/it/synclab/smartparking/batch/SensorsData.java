package it.synclab.smartparking.batch;

import lombok.Data;

@Data
public class SensorsData
{
	private String date;
	private String time;
	private String pm25Values;
	private String pm10Values;
	private String tempValues;
	private String humidValues;
}
