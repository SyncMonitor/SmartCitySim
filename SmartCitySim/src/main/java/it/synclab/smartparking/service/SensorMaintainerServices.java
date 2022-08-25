package it.synclab.smartparking.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.synclab.smartparking.model.Maintainer;
import it.synclab.smartparking.model.Marker;
import it.synclab.smartparking.repository.SensorsMaintainerRepository;
import it.synclab.smartparking.repository.model.SensorsMaintainer;

@Component
public class SensorMaintainerServices {
	
	@Autowired
	private SensorsMaintainerRepository sensorsMaintainerRepository;

	private static final Logger logger = LogManager.getLogger(SensorMaintainerServices.class);

	
	public SensorsMaintainer buildSensorsMaintainerFromMarker(Marker marker) {
		logger.debug("SensorMaintainerServices START buildSensorsMaintainerFromMarker");
		SensorsMaintainer sensorsMaintainer = new SensorsMaintainer();
		if (marker.getId() != null) {
			sensorsMaintainer.setFkSensorId(marker.getId());
		}
//		Type attribute missing in XML file
//		if(m.getType() != null) {}
		sensorsMaintainer.setType("ParkingArea");
		logger.debug("SensorMaintainerServices END buildSensorsMaintainerFromMarker");
		return sensorsMaintainer;
	}

	public void saveSensorsMaintainerData(SensorsMaintainer sensorsMaintainer) {
		logger.debug("SensorMaintainerServices START saveSensorsMaintainerData");
		try {
			sensorsMaintainerRepository.save(sensorsMaintainer);
		} catch (Exception e) {
			logger.error("SensorMaintainerServices - Error", e);
		}
		logger.debug("SensorMaintainerServices END saveSensorsMaintainerData");
	}

	public List<SensorsMaintainer> getAllSensorsMaintainerData() {
		logger.debug("SensorMaintainerServices START getAllSensorsMaintainerData");
		List<SensorsMaintainer> maintainers = sensorsMaintainerRepository.getAllSensorsMaintainerData();
		logger.debug("SensorMaintainerServices END getAllSensorsMaintainerData");
		return maintainers;
	}

	public List<SensorsMaintainer> getSensorsMaintainerDataBySensorId(Long sensorId) {
		logger.debug("SensorMaintainerServices START getSensorsMaintainerDataBySensorId");
		List<SensorsMaintainer> maintainers = sensorsMaintainerRepository.getSensorsMaintainersByFkSensorId(sensorId);
		logger.debug("SensorMaintainerServices END getSensorsMaintainerDataBySensorId");
		return maintainers;
	}

	public SensorsMaintainer getSensorsMaintainerDataById(Long id) {
		logger.debug("SensorMaintainerServices START getSensorsMaintainerDataById");
		SensorsMaintainer maintainers = sensorsMaintainerRepository.getSensorsMaintainerById(id);
		logger.debug("SensorMaintainerServices END getSensorsMaintainerDataById");
		return maintainers;
	}

	public void updateSensorsMaintainerDataBySensorId(Maintainer maintainer, Long sensorId) {
		logger.debug("SensorMaintainerServices START updateSensorsMaintainerDataBySensorId");
		List<SensorsMaintainer> maintainers = getSensorsMaintainerDataBySensorId(sensorId);
		for (SensorsMaintainer m : maintainers) {
			if (maintainer.getName() != null) {
				sensorsMaintainerRepository.updateNameById(maintainer.getName(), m.getId());
			}
			if (maintainer.getSurname() != null) {
				sensorsMaintainerRepository.updateSurnameById(maintainer.getSurname(), m.getId());
			}
			if (maintainer.getCompany() != null) {
				sensorsMaintainerRepository.updateCompanyById(maintainer.getCompany(), m.getId());
			}
			if (maintainer.getPhoneNumber() != null) {
				sensorsMaintainerRepository.updatePhoneNumberById(maintainer.getPhoneNumber(), m.getId());
			}
			if (maintainer.getMail() != null) {
				sensorsMaintainerRepository.updateMailById(maintainer.getMail(), m.getId());
			}
		}
		logger.debug("SensorMaintainerServices END updateSensorsMaintainerDataBySensorId");
	}

	public void updateSensorsMaintainerDataById(Maintainer maintainer, Long id) {
		logger.debug("SensorMaintainerServices START updateSensorsMaintainerDataById");
		if (maintainer.getName() != null) {
			sensorsMaintainerRepository.updateNameById(maintainer.getName(), id);
		}
		if (maintainer.getSurname() != null) {
			sensorsMaintainerRepository.updateSurnameById(maintainer.getSurname(), id);
		}
		if (maintainer.getCompany() != null) {
			sensorsMaintainerRepository.updateCompanyById(maintainer.getCompany(), id);
		}
		if (maintainer.getPhoneNumber() != null) {
			sensorsMaintainerRepository.updatePhoneNumberById(maintainer.getPhoneNumber(), id);
		}
		if (maintainer.getMail() != null) {
			sensorsMaintainerRepository.updateMailById(maintainer.getMail(), id);
		}
		logger.debug("SensorMaintainerServices END updateSensorsMaintainerDataById");
	}

	public void updateSensorMaintainerToBeRepairedBySensorId(boolean toBeRepaired, Long sensorId) {
		logger.debug("SensorMaintainerServices START updateSensorMaintainerToBeRepairedBySensorId");
		sensorsMaintainerRepository.updateToBeRepairedBySensorId(toBeRepaired, sensorId);
		logger.debug("SensorMaintainerServices END updateSensorMaintainerToBeRepairedBySensorId");
	}

	public void updateSensorMaintainerToBeChargedBySensorId(boolean toBeCharged, Long sensorId) {
		logger.debug("SensorMaintainerServices START updateSensorMaintainerToBeChargeddBySensorId");
		sensorsMaintainerRepository.updateToBeChargedBySensorId(toBeCharged, sensorId);
		logger.debug("SensorMaintainerServices END updateSensorMaintainerToBeChargeddBySensorId");
	}

	public void deleteSensorMaintainersBySensorId(Long sensorId) {
		logger.debug("SensorMaintainerServices deleteSensorMaintainersBySensorId deleteAlSensors ");
		sensorsMaintainerRepository.deleteSensorMaintainersBySensorId(sensorId);
		logger.debug("SensorMaintainerServices END deleteSensorMaintainersBySensorId ");
	}

	public void deleteSensorMaintainersById(Long id) {
		logger.debug("SensorMaintainerServices START deleteSensorMaintainersById ");
		sensorsMaintainerRepository.deleteSensorMaintainersById(id);
		logger.debug("SensorMaintainerServices END deleteSensorMaintainersById ");
	}

	public void deleteAllSensorMaintainers() {
		logger.debug("SensorMaintainerServices START deleteAlSensors ");
		sensorsMaintainerRepository.deleteAll();
		logger.debug("SensorMaintainerServices END deleteAlSensors ");
	}
}
