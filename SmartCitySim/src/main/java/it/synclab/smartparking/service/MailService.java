package it.synclab.smartparking.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailService {

	@Autowired
	JavaMailSender javaMailSender;
	
	@Value("$mail.reciver")
	String mailReciver;
	
	@Value("$mail.reciver2")
	String mailReciver2;
	
	@Value("$mail.message.low.battery")
	String messageLowBattery;
	
	@Value("$mail.message.sensor.off")
	String messageSensorOff;
	
	

	private static final Logger logger = LogManager.getLogger(MailService.class);

	public void sendEmail() {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(mailReciver2);
        msg.setSubject("Sensore Scarico");
        msg.setText("La batteria del sensore è scarica");
        msg.setFrom("smartcitysimulator@gmail.com");
        logger.info("Mail sent");
        

        javaMailSender.send(msg);

    }	
	
}
