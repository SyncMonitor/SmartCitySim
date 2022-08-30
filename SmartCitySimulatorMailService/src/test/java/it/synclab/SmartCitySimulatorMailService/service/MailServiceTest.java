package it.synclab.SmartCitySimulatorMailService.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;


@SpringBootTest
public class MailServiceTest {

	@Autowired
    private MailService mailService;

	//Not real assertion, just to check if the service is working
	@Test
	public void messageListenerTest() {
		String msg = "{\"from\":\"smartcitysimulator@gmail.com\",\"replyTo\":null,\"to\":[\"zeno97@live.it\"],\"cc\":null,\"bcc\":null,\"sentDate\":null,\"subject\":\"Test Subject\",\"text\":\"Test Mail Text\"}";
		mailService.messageListener(msg);
		Assert.assertTrue(true);
	}

	//Not real assertion, just to check if the service is working
	@Test
	public void messageListenerExceptiopnTest() {
		String msg = "notJsonFormatString";
		mailService.messageListener(msg);
		Assert.assertTrue(true);
	}	
}
