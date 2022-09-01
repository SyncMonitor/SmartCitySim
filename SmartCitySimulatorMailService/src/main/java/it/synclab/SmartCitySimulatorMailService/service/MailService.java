package it.synclab.SmartCitySimulatorMailService.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.synclab.SmartCitySimulatorMailService.model.MailParameter;



@Component
public class MailService {

    @Autowired
    JavaMailSender javaMailSender;

    private static final Logger logger = LogManager.getLogger(MailService.class);

    @JmsListener(destination = "scs-mail-queue")
    public void messageListener(String msg) {
        logger.info("MailService - messageListener() - message recived: {}", msg);
        ObjectMapper mapper = new ObjectMapper();
        try {
            MailParameter mailParameter = mapper.readValue(msg, MailParameter.class);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailParameter.getFrom());
            message.setTo(mailParameter.getTo());
            message.setSubject(mailParameter.getSubject());
            message.setText(mailParameter.getText());
            javaMailSender.send(message);
        } catch (JsonProcessingException e) {
            logger.error("error in sending mail with parameters: {}", msg);
        }
    }
}
