package it.synclab.smartparking.service;

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
import it.synclab.smartparking.repository.model.ParkingArea;
import it.synclab.smartparking.repository.model.Sensor;

@Component
public class SensorServices {
	
	@Value("${sensor.parking.url}")
	private String sensorDataUrl;
	
	@Autowired
	private ParkingAreaServices parkingAreaServices;
		
	@Autowired
	private SensorsRepository sensorsRepository;
	
	private static final Logger logger = LogManager.getLogger(SensorServices.class);

	public void saveSensorData(Sensor sensor) {
		logger.debug("SensorServices START saveSensorData");
		try {
			sensorsRepository.save(sensor);
		} catch (Exception e) {
			logger.error("SensorServices - Error", e);
		}
		logger.debug("SensorServices END saveSensorData");
	}

	public Sensor buildSensorFromMarker(Marker marker) {
		logger.debug("SensorServices START buildSensorFromMarker");
		Sensor s = new Sensor();
		List<ParkingArea> parkArea = new ArrayList<>();
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
		ParkingArea p = parkingAreaServices.buildParkingAreaFromMarker(marker);
		parkArea.add(p);
		s.setParkingArea(parkArea);
		logger.debug("SensorServices END buildSensorFromMarker");
		return s;
	}

	public List<Sensor> getAllSensorsFromDB() {
		logger.debug("SensorServices START getAllSensorsFromDB");
		List<Sensor> sensors = sensorsRepository.getAllSensorFromDB();
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

	public List<Sensor> getSensorsByName(String name) {
		logger.debug("SensorServices START getSensorsByName - sensorName:{}", name);
		List<Sensor> sensors = sensorsRepository.getSensorsByName(name);
		logger.debug("SensorServices END getSensorByName - snesorName:{} - sensorsListSize:{}", sensors.size());
		return sensors;
	}

	public Sensor getSensorById(Long id) {
		logger.debug("SensorServices START getSensorById - sensorId:{}", id);
		Sensor s = sensorsRepository.getSensorById(id);
		logger.debug("ParkingService END getSensorById - sensorName:{} - sensorId:{}", s.getName(), s.getId());
		return s;
	}

	public List<Sensor> getSensorsByType(String type) {
		logger.debug("ParkingService START getSensorsByType -sensorType");
		List<Sensor> sensors = sensorsRepository.getSensorsByType(type);
		logger.debug("ParkingService END getSensorsByType - sensorsType:{} - sensorsListSize:{}", type, sensors.size());
		return sensors;
	}

	public List<Sensor> getSensorByNameStartingWith(String str) {
		logger.debug("ParkingService START getSensorByNameStartingWith - startingSequence:{}", str);
		List<Sensor> sensors = sensorsRepository.getSensorByNameStartingWith(str);
		logger.debug("ParkingService END getSensorByNameStartingWith - startingSequence:{} - sensorsListSize:{}", str,
				sensors.size());
		return sensors;
	}

	public List<Sensor> getSensorByNameEndingWith(String str) {
		logger.debug("ParkingService START getSensorByNameEndingWith - endingSequence:{}", str);
		List<Sensor> sensors = sensorsRepository.getSensorByNameEndingWith(str);
		logger.debug("ParkingService END getSensorByNameEndingWith -  - startingSequence:{} - sensorsListSize:{}", str,
				sensors.size());
		return sensors;
	}

	public List<Sensor> getSensorByNameContaining(String str) {
		logger.debug("ParkingService START getSensorByNameContaining - Sequence:{}", str);
		List<Sensor> sensors = sensorsRepository.getSensorByNameContaining(str);
		logger.debug("ParkingService END getSensorByNameContaining - Sequence:{} - sensorsListSize:{}", str,
				sensors.size());
		return sensors;
	}

	public List<Sensor> getSensorByIsActiveTrue() {
		logger.debug("ParkingService START getSensorByIsActiveTrue");
		List<Sensor> sensors = sensorsRepository.getSensorByIsActiveTrue();
		logger.debug("ParkingService END getSensorByIsActiveTrue - sensorsListSize:{}", sensors.size());
		return sensors;
	}

	public List<Sensor> getSensorByIsActiveFalse() {
		logger.debug("ParkingService START getSensorByIsActiveFalse");
		List<Sensor> sensors = sensorsRepository.getSensorByIsActiveFalse();
		logger.debug("SensorServices END getSensorByIsActiveFalse - sensorsListSize:{}", sensors.size());
		return sensors;
	}

	public List<Sensor> getLowBatterySensors(MarkerList sensors) {
		List<Sensor> lowBatterySensors = new ArrayList<>();
		for (Marker m : sensors.getMarkers().getMarkers()) {
			String battery = m.getBattery().substring(0, 3).replace(',', '.');
			Float b = Float.parseFloat(battery);
			if (b < 3) {
				Sensor corruptedSensor = buildSensorFromMarker(m);
				lowBatterySensors.add(corruptedSensor);
			}
		}
		return lowBatterySensors;
	}

	public List<Sensor> getCorruptedSensors(MarkerList sensors) {
		List<Sensor> corruptedSensors = new ArrayList<>();
		for (Marker m : sensors.getMarkers().getMarkers()) {
			if (!m.isActive()) {
				Sensor corruptedSensor = buildSensorFromMarker(m);
				corruptedSensors.add(corruptedSensor);
			}
		}
		return corruptedSensors;
	}

	public void updateSensorNameById(String name, Long sensorId) {
		logger.debug("SensorServices START updateSensorNameById - sensorName:{} - sensorId:{}", name, sensorId);
		sensorsRepository.updateSensorName(name, sensorId);
		logger.debug("SensorServices END updateSensorNameById - sensorName:{} - sensorId:{}", name, sensorId);

	}

	public void updateSensorBatteryById(String battery, Long sensorId) {
		logger.debug("SensorServices START updateSensorBatteryById - sensorBattery:{} - sensorId:{}", battery,
				sensorId);
		sensorsRepository.updateSensorBattery(battery, sensorId);
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

	public void deleteSensorById(Long sensorId) {
		logger.debug("SensorServices START deleteSensorById - sensorId:{}", sensorId);
		sensorsRepository.deleteById(sensorId);
		logger.debug("SensorServices END deleteSensorById - sensorId:{}", sensorId);

	}

	public void deleteAllSensors() {
		logger.debug("SensorServices START deleteAlSensors ");
		sensorsRepository.deleteAll();
		logger.debug("SensorServices END deleteAlSensors ");
	}
}
