package it.synclab.smartparking.service;


import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import it.synclab.datasource.config.PostgreClient;
import it.synclab.smartparking.model.MarkerList;

@Service
public class ParkingService {
	
	private String sensorDataUrl = "https://syncmonitor.altervista.org/smartparking/test1.xml";

	@Autowired
	PostgreClient databaseClient;
	
	/*
	 * 0 free 1 occupy
	 **/
	public int getSensorState(String sensorId) {
		// Read data from database
		return 0;
	}

	public MarkerList convertXMLtoJson(String xml) {

		MarkerList markersList = null;

		try {
			JSONObject jsonObj = XML.toJSONObject(xml);
			String object = jsonObj.toString();
			ObjectMapper objectMapper = new ObjectMapper();
			markersList = objectMapper.readValue(object, MarkerList.class);

		} catch (Exception e) {
			System.out.println(e);
		}

		return markersList;
	}

	public MarkerList readSensorData() throws Exception {

		MarkerList markersList = null;

		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(this.sensorDataUrl).build();
			Response response = client.newCall(request).execute();
			String xmlSensorData = response.body().string();
			markersList = convertXMLtoJson(xmlSensorData);
		} catch (Exception e) {
			System.out.println(e);
		}

		return markersList;
	}

}
