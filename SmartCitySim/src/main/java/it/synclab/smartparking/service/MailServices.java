package it.synclab.smartparking.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.synclab.smartparking.repository.model.ParkingSensors;
import it.synclab.smartparking.repository.model.Sensors;
import it.synclab.smartparking.repository.model.SensorsMaintainer;

@Component
public class MailServices {

	@Value("${mail.message.low.battery.start}")
	private String lowBatteryStartMessage;

	@Value("${mail.message.sensor.off.start}")
	private String sensorOffStartMessage;

	@Value("${mail.message.sensor.not.updating.start}")
	private String notUpdatingStartMessage;

	@Value("${mail.message.low.battery.end}")
	private String lowBatteryEndMessage;

	@Value("${mail.message.sensor.off.end}")
	private String sensorOffEndMessage;
	
	@Value("${mail.message.sensor.not.updating.end}")
	private String notUpdatingEndMessage;

	@Value("${mail.subject.battery.low}")
	private String lowBatterySubject;

	@Value("${mail.subject.sensor.off}")
	private String sensorOffSubject;

	@Value("${mail.subject.sensor.not.updating}")
	String notUpdatingSubject;

	@Value("${mail.activeMq.queue}")
	String queue;

	@Autowired
	JmsTemplate jmsTemplate;

	@Autowired
	SensorsServices sensorService;

	@Autowired
	ParkingSensorsServices parkingSensorsService;

	@Autowired
	SensorMaintainerServices sensorMaintainerServices;

	@Value("${mail.sender}")
	private String mailSender;

	private static final Logger logger = LogManager.getLogger(MailServices.class);

	public void sendEmailParametersToQue(String email, String subject, String text) {
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(email);
			msg.setSubject(subject);
			msg.setText(text);
			msg.setFrom(mailSender);

			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(msg);
			jmsTemplate.convertAndSend(queue,json);
		} catch (Exception e) {
			logger.warn("mailService - ERROR in sent mail for subject {}", subject);
		}
	}

	public String printMail(Sensors s, ParkingSensors parkingSensor) {
		String text = "Id = " + s.getId() + ", Name = " + s.getName() + ", ";
			text += "Address = " + parkingSensor.getAddress() + ", Latitude = " + parkingSensor.getLatitude()
					+ ", Longitude = " + parkingSensor.getLongitude();
		return text;
	}

	public void sendCorruptedSensorsMail() {
		List<SensorsMaintainer> maintainers;
		maintainers = sensorMaintainerServices.getAllSensorsMaintainerData();
		for (SensorsMaintainer m : maintainers) {
			if (m.isToBeRepaired()) {
				Sensors s = sensorService.getSensorById(m.getFkSensorId());
				logger.debug("mail: " + m.getMail());
				String sensorOff = "";
				sensorOff += printMail(s, s.getParkingSensors()) + "\n\n";
				String sensorOffMessage = sensorOffStartMessage + sensorOff + sensorOffEndMessage;
				sendEmailParametersToQue(m.getMail(), sensorOffSubject + " " + s.getName(), sensorOffMessage);
				m.setToBeRepaired(false);
			}
		}
	}

	public void sendLowBatterySensorsMail() {
		List<SensorsMaintainer> maintainers;
		maintainers = sensorMaintainerServices.getAllSensorsMaintainerData();
		for (SensorsMaintainer m : maintainers) {
			if (m.isToBeCharged()) {
				Sensors s = sensorService.getSensorById(m.getFkSensorId());
				logger.debug("mail: " + m.getMail());
				String lowBattery = "";
				lowBattery += printMail(s, s.getParkingSensors()) + "\n\n";
				String lowBatterySensorMessage = lowBatteryStartMessage + lowBattery + lowBatteryEndMessage;
				sendEmailParametersToQue(m.getMail(), lowBatterySubject + " " + s.getName(), lowBatterySensorMessage);
				m.setToBeCharged(false);
			}
		}
	}

	public void sendNotUpdatingSensorsMail() {
		List<SensorsMaintainer> maintainers;
		maintainers = sensorMaintainerServices.getAllSensorsMaintainerData();
		for (SensorsMaintainer m : maintainers) {
			if (!m.isUpdating()) {
				Sensors s = sensorService.getSensorById(m.getFkSensorId());
				logger.debug("mail: " + m.getMail());
				String notUpdating = "";
				notUpdating += printMail(s, s.getParkingSensors()) + "\n\n";
				String notUpdatingSensorMessage = notUpdatingStartMessage + notUpdating + notUpdatingEndMessage;
				sendEmailParametersToQue(m.getMail(), notUpdatingSubject + " " + s.getName(), notUpdatingSensorMessage);
				m.setIsUpdating(true);
			}
		}
	}
}
