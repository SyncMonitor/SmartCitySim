package it.synclab.smartparking.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import it.synclab.smartparking.repository.model.ParkingArea;
import it.synclab.smartparking.repository.model.Sensor;
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

	@Autowired
	JavaMailSender javaMailSender;

	@Autowired
	SensorServices sensorService;

	@Autowired
	ParkingAreaServices parkingService;

	@Autowired
	SensorMaintainerServices sensorMaintainerServices;

	@Value("${mail.sender}")
	private String mailSender;

	private static final Logger logger = LogManager.getLogger(MailServices.class);

	public void sendEmail(String email, String subject, String text) {
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(email);
			msg.setSubject(subject);
			msg.setText(text);
			msg.setFrom(mailSender);
			// Inserire messaggio nella coda (Listener) -> da dentro la coda mando la mail
			javaMailSender.send(msg);
			logger.info("Mail sent");
		} catch (Exception e) {
			logger.warn("mailService - ERROR in sent mail for subject {}", subject);
		}
	}

	public String printMail(Sensor s, List<ParkingArea> parkingArea) {
		String text = "Id = " + s.getId() + ", Name = " + s.getName() + ", ";
		for (int i = 0; i < parkingArea.size(); i++) {
			text += "Address = " + parkingArea.get(i).getAddress() + ", Latitude = " + parkingArea.get(i).getLatitude()
					+ ", Longitude = " + parkingArea.get(i).getLongitude();
		}
		return text;
	}

	public void sendCorruptedSensorsMail() {
		List<SensorsMaintainer> maintainers;
		maintainers = sensorMaintainerServices.getAllSensorsMaintainerData();
		for (SensorsMaintainer m : maintainers) {
			if (m.isToBeRepaired()) {
				Sensor s = sensorService.getSensorById(m.getFkSensorId());
				logger.debug("mail: " + m.getMail());
				String sensorOff = "";
				sensorOff += printMail(s, s.getParkingArea()) + "\n\n";
				String sensorOffMessage = sensorOffStartMessage + sensorOff + sensorOffEndMessage;
				sendEmail(m.getMail(), sensorOffSubject + " " + s.getName(), sensorOffMessage);
				m.setToBeRepaired(false);
			}
		}
	}

	public void sendLowBatterySensorsMail() {
		List<SensorsMaintainer> maintainers;
		maintainers = sensorMaintainerServices.getAllSensorsMaintainerData();
		for (SensorsMaintainer m : maintainers) {
			if (m.isToBeCharged()) {
				Sensor s = sensorService.getSensorById(m.getFkSensorId());
				logger.debug("mail: " + m.getMail());
				String lowBattery = "";
				lowBattery += printMail(s, s.getParkingArea()) + "\n\n";
				String lowBatterySensorMessage = lowBatteryStartMessage + lowBattery + lowBatteryEndMessage;
				sendEmail(m.getMail(), lowBatterySubject + " " + s.getName(), lowBatterySensorMessage);
				m.setToBeCharged(false);
			}
		}
	}

	public void sendNotUpdatingSensorsMail() {
		List<SensorsMaintainer> maintainers;
		maintainers = sensorMaintainerServices.getAllSensorsMaintainerData();
		for (SensorsMaintainer m : maintainers) {
			if (!m.isUpdating()) {
				Sensor s = sensorService.getSensorById(m.getFkSensorId());
				logger.debug("mail: " + m.getMail());
				String notUpdating = "";
				notUpdating += printMail(s, s.getParkingArea()) + "\n\n";
				String notUpdatingSensorMessage = notUpdatingStartMessage + notUpdating + notUpdatingEndMessage;
				sendEmail(m.getMail(), notUpdatingSubject + " " + s.getName(), notUpdatingSensorMessage);
				m.setIsUpdating(true);
			}
		}
	}
}
