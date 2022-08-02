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
	
	@Value("${mail.sender}")
	private String mailSender;
	
	

	private static final Logger logger = LogManager.getLogger(MailService.class);

	public void sendEmail(String email, String subject, String text) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(text);
        msg.setFrom(mailSender);
        logger.info("Mail sent");
        

        javaMailSender.send(msg);

    }	
	
}
