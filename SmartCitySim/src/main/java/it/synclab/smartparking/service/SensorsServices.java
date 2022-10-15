package it.synclab.smartparking.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.synclab.smartparking.model.Marker;
import it.synclab.smartparking.model.MarkerList;
import it.synclab.smartparking.repository.SensorsRepository;
import it.synclab.smartparking.repository.model.ParkingSensors;
import it.synclab.smartparking.repository.model.Sensors;

@Component
public class SensorsServices {
	
	@Value("${sensor.parking.url}")
	private String sensorDataUrl;
	
	@Autowired
	private ParkingSensorsServices parkingSensorsServices;
		
	@Autowired
	private SensorsRepository sensorsRepository;
	
	private static final Logger logger = LogManager.getLogger(SensorsServices.class);

	public void saveSensorData(Sensors sensor) {
		logger.debug("SensorServices START saveSensorData");
		try {
			sensorsRepository.save(sensor);
		} catch (Exception e) {
			logger.error("SensorServices - Error", e);
		}
		logger.debug("SensorServices END saveSensorData");
	}

	public Sensors buildSensorFromMarker(Marker marker) {
		logger.debug("SensorServices START buildSensorFromMarker");
		Sensors s = new Sensors();
		ParkingSensors parkSensors = new ParkingSensors();
		if (marker.getId() != null) {
			s.setId(marker.getId());
		}
		if (marker.getName() != null) {
			s.setName(marker.getName());
		}
		if (marker.getBattery() != null) {
			s.setBattery(marker.getBattery());
		}
		if (marker.getBattery() != null) {
			s.setCharge(marker.getBattery().substring(0, 1));
		}
//		Type attribute missing in XML file
//		if(m.getType() != null) {}
		s.setType("ParkingArea");
		s.setActive(marker.isActive());
		s.setLastSurvey(LocalDateTime.now());
		ParkingSensors p = parkingSensorsServices.buildParkingSensorFromMarker(marker);
		s.setParkingSensors(parkSensors);
		logger.debug("SensorServices END buildSensorFromMarker");
		return s;
	}

	public List<Sensors> getAllSensorsFromDB() {
		logger.debug("SensorService"
				+ ""
				+ ""
				+ ""
				+ ""
				+ ""
				+ ""
				+ ""
				+ ""
				+ ""
				+ ""
				+ ""
				+ ""
				+ ""
				+ ""
				+ ""
				+ ""
				+ ""
				+ "s START getAllSensorsFromDB");
		List<Sensors> sensors = sensorsRepository.getAllSensorFromDB();
		logger.debug("SensorServices END getAllSensorsFromDB");
		return sensors;
	}

	// state = 0 : notWorking, 1 : working
	public boolean getSensorState(Long sensorId) {
		logger.debug("SensorServices START getSensorState - sensorId{}", sensorId);
		boolean state = sensorsRepository.getSensorState(sensorId);
		logger.debug("SensorServices END getSensorState - sensorState:{} - sensorId:{}", state, sensorId);
		return state;
	}

	public List<Sensors> getSensorsByName(String name) {
		logger.debug("SensorServices START getSensorsByName - sensorName:{}", name);
		List<Sensors> sensors = sensorsRepository.getSensorsByName(name);
		logger.debug("SensorServices END getSensorByName - snesorName:{} - sensorsListSize:{}", name, sensors.size());
		return sensors;
	}

	public Sensors getSensorById(Long id) {
		logger.debug("SensorServices START getSensorById - sensorId:{}", id);
		Sensors s = sensorsRepository.getSensorById(id);
		logger.debug("ParkingService END getSensorById - sensorName:{} - sensorId:{}", s.getName(), s.getId());
		return s;
	}

	public List<Sensors> getSensorsByType(String type) {
		logger.debug("ParkingService START getSensorsByType -sensorType");
		List<Sensors> sensors = sensorsRepository.getSensorsByType(type);
		logger.debug("ParkingService END getSensorsByType - sensorsType:{} - sensorsListSize:{}", type, sensors.size());
		return sensors;
	}

	public List<Sensors> getSensorByNameStartingWith(String str) {
		logger.debug("ParkingService START getSensorByNameStartingWith - startingSequence:{}", str);
		List<Sensors> sensors = sensorsRepository.getSensorByNameStartingWith(str);
		logger.debug("ParkingService END getSensorByNameStartingWith - startingSequence:{} - sensorsListSize:{}", str,
				sensors.size());
		return sensors;
	}

	public List<Sensors> getSensorByNameEndingWith(String str) {
		logger.debug("ParkingService START getSensorByNameEndingWith - endingSequence:{}", str);
		List<Sensors> sensors = sensorsRepository.getSensorByNameEndingWith(str);
		logger.debug("ParkingService END getSensorByNameEndingWith -  - startingSequence:{} - sensorsListSize:{}", str,
				sensors.size());
		return sensors;
	}

	public List<Sensors> getSensorByNameContaining(String str) {
		logger.debug("ParkingService START getSensorByNameContaining - Sequence:{}", str);
		List<Sensors> sensors = sensorsRepository.getSensorsByNameContaining(str);
		logger.debug("ParkingService END getSensorByNameContaining - Sequence:{} - sensorsListSize:{}", str,
				sensors.size());
		return sensors;
	}

	public List<Sensors> getSensorByIsActiveTrue() {
		logger.debug("ParkingService START getSensorByIsActiveTrue");
		List<Sensors> sensors = sensorsRepository.getSensorsByIsActiveTrue();
		logger.debug("ParkingService END getSensorByIsActiveTrue - sensorsListSize:{}", sensors.size());
		return sensors;
	}

	public List<Sensors> getSensorByIsActiveFalse() {
		logger.debug("ParkingService START getSensorByIsActiveFalse");
		List<Sensors> sensors = sensorsRepository.getSensorsByIsActiveFalse();
		logger.debug("SensorServices END getSensorByIsActiveFalse - sensorsListSize:{}", sensors.size());
		return sensors;
	}

	public List<Sensors> getLowBatterySensors(MarkerList sensors) {
		List<Sensors> lowBatterySensors = new ArrayList<>();
		for (Marker m : sensors.getMarkers().getMarkers()) {
			String battery = m.getBattery().substring(0, 3).replace(',', '.');
			Float b = Float.parseFloat(battery);
			if (b < 3) {
				Sensors corruptedSensor = buildSensorFromMarker(m);
				lowBatterySensors.add(corruptedSensor);
			}
		}
		return lowBatterySensors;
	}

	public List<Sensors> getCorruptedSensors(MarkerList sensors) {
		List<Sensors> corruptedSensors = new ArrayList<>();
		for (Marker m : sensors.getMarkers().getMarkers()) {
			if (!m.isActive()) {
				Sensors corruptedSensor = buildSensorFromMarker(m);
				corruptedSensors.add(corruptedSensor);
			}
		}
		return corruptedSensors;
	}

	public void updateSensorNameById(String name, Long sensorId) {
		logger.debug("SensorServices START updateSensorNameById - sensorName:{} - sensorId:{}", name, sensorId);
		sensorsRepository.updateSensorsName(name, sensorId);
		logger.debug("SensorServices END updateSensorNameById - sensorName:{} - sensorId:{}", name, sensorId);

	}

	public void updateSensorBatteryById(String battery, Long sensorId) {
		logger.debug("SensorServices START updateSensorBatteryById - sensorBattery:{} - sensorId:{}", battery,
				sensorId);
		sensorsRepository.updateSensorsBattery(battery, sensorId);
		logger.debug("SensorServices END updateSensorBatteryById - sensorBattery:{} - sensorId:{}", battery, sensorId);
	}

	public void updateSensorChargeById(String charge, Long sensorId) {
		logger.debug("SensorServices START updateSensorChargeById - charge:{} - sensorId:{}", charge, sensorId);
		sensorsRepository.updateSensorChargeById(charge, sensorId);
		logger.debug("SensorServices END updateSensorChargeById - charge:{} - sensorId:{}", charge, sensorId);
	}

	public void updateSensorTypeById(String type, Long sensorId) {
		logger.debug("SensorServices START updateSensorTypeById - sensorType:{} - sensorId:{}", type, sensorId);
		sensorsRepository.updateSensorTypeById(type, sensorId);
		logger.debug("SensorServices END updateSensorTypeById - sensorType:{} - sensorId:{}", type, sensorId);
	}

	public void updateSensorStateById(boolean state, Long id) {
		logger.debug("SensorServices START updateSensorStateById - sensorState:{} - sensorId:{}", state, id);
		sensorsRepository.updateStateById(state, id);
		logger.debug("SensorServices END updateSensorStateById - sensorState:{} - sensorId:{}", state, id);
	}

	public void updateSensorLastSurveyById(Long id) {
		logger.debug("SensorServices START updateSensorLastUpdateById - sensorId:{}", id);
		sensorsRepository.updateLastSurveyById(LocalDateTime.now(), id);
		logger.debug("SensorServices END updateSensorLastUpdateById - sensorId:{}", id);
	}

	public void deleteSensorById(Long sensorId) {
		logger.debug("SensorServices START deleteSensorById - sensorId:{}", sensorId);
		sensorsRepository.deleteById(sensorId);
		logger.debug("SensorServices END deleteSensorById - sensorId:{}", sensorId);

	}

	public void deleteAllSensors() {
		logger.debug("SensorServices START deleteAllSensors ");
		sensorsRepository.deleteAll();
		logger.debug("SensorServices END deleteAllSensors ");
	}
}
