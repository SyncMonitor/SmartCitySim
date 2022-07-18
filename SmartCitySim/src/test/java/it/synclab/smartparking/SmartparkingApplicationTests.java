package it.synclab.smartparking;

import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


@SpringBootTest
class SmartparkingApplicationTests {

	@Test
	void contextLoads() {
	}
	


    @Test
    public void readXmlSensorData() throws Exception {
        try {
        	OkHttpClient client = new OkHttpClient();
        	Request request = new Request.Builder().url("https://syncmonitor.altervista.org/smartparking/test1.xml").build();
        	Response response = client.newCall(request).execute();
        	System.out.println(response.body().string());
        }catch(Exception e) {
        	
        }
    }

}
