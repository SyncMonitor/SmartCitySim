package it.synclab.smartparking;

import org.json.JSONObject;
import org.json.XML;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import it.synclab.smartparking.model.MarkerList;

 

public class ReadData {
	

	public MarkerList convertXML(String xml) {
        
		MarkerList markersList = null;
		try {
        	final int PRETTY_PRINT_INDENT_FACTOR = 4;
        	
//        	String xml = IOUtils.toString(this.getClass().getResourceAsStream("/Markers.xml"), "UTF-8");
        	
            JSONObject jsonObj = XML.toJSONObject(xml);
            String object = jsonObj.toString();
            System.out.println(jsonObj.toString(PRETTY_PRINT_INDENT_FACTOR));
            
            ObjectMapper objectMapper = new ObjectMapper();
            markersList = objectMapper.readValue(object, MarkerList.class);
            //System.out.println(markersList.getMarkers().getMarker().get(10).getId());
            
//            JSONObject tmp = new JSONObject(object);
//            System.out.println(markersList.toString());
           
            
        } catch (Exception e) {
            System.out.println(e);
        }
        return markersList;
    }
	
	
	@Test
    public void readXmlSensorData() throws Exception {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://syncmonitor.altervista.org/smartparking/test1.xml").build();
            Response response = client.newCall(request).execute();
            String xmlSensorData = response.body().string();
//            System.out.println(xmlSensorData);
            
            MarkerList markersList = convertXML(xmlSensorData);
            
            System.out.println(markersList.toString());
            
        }catch(Exception e) {

        }
    }
}
