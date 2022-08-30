package it.synclab.SmartCitySimulatorMailService.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class MailParameterTest {

	@Test
	public void MailParameterTest() {
        String from = "testFrom";
        String replyTo = "testReplyTo";
        String[] to = {"testTo1","testTo2"};
        String[] cc = {"testCc"};
        String[] bcc = {"testBcc"};
        Date date = new Date();
        String testSubject = "testSubject";
        String testText = "testText";
        MailParameter result = new MailParameter(from, replyTo, to, cc, bcc, date, testSubject, testText);
        MailParameter aux = new MailParameter();
        aux.setFrom(from);
        aux.setReplyTo(replyTo);
        aux.setTo(to);
        aux.setCc(cc);
        aux.setBcc(bcc);
        aux.setSentDate(date);
        aux.setSubject(testSubject);
        aux.setText(testText);
        Assert.assertEquals(result.getFrom(), aux.getFrom());
        Assert.assertEquals(result.getReplyTo(), aux.getReplyTo());
        Assert.assertEquals(result.getTo(), aux.getTo());
        Assert.assertEquals(result.getCc(), aux.getCc());
        Assert.assertEquals(result.getBcc(), aux.getBcc());
        Assert.assertEquals(result.getSentDate(), aux.getSentDate());
        Assert.assertEquals(result.getSubject(), aux.getSubject());
        Assert.assertEquals(result.getText(), aux.getText());
        Assert.assertEquals(result.toString(), aux.toString());
    }
}