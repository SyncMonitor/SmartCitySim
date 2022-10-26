package it.synclab.smartparking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.synclab.smartparking.repository.model.MaintainersRegistry;

@Transactional
@Repository
public interface SensorsMaintainerRepository extends CrudRepository<MaintainersRegistry, Long>{


	MaintainersRegistry getSensorsMaintainerById(Long id);

	List<MaintainersRegistry> getSensorsMaintainersByFkSensorId(Long sensorId);
	
	@Query("select m from MaintainersRegistry m")
	List<MaintainersRegistry> getAllSensorsMaintainerData();
	
	@Modifying
	@Query("update MaintainersRegistry s set s.ownerName = ?1 where id = ?2")
	public void updateNameById(String name, Long id);
	
	@Modifying
	@Query("update MaintainersRegistry s set s.ownerSurname = ?1 where id = ?2")
	public void updateSurnameById(String surname, Long id);
	
	@Modifying
	@Query("update MaintainersRegistry s set s.phoneNumber = ?1 where id = ?2")
	public void updatePhoneNumberById(String phoneNumber, Long id);
	
	@Modifying
	@Query("update MaintainersRegistry s set s.mail = ?1 where id = ?2")
	public void updateMailById(String mail, Long id);
	
	@Modifying
	@Query("update MaintainersRegistry s set s.company = ?1 where id = ?2")
	public void updateCompanyById(String company, Long id);
	
	@Modifying
	@Query("update MaintainersRegistry s set s.toBeRepaired = ?1 where fkSensorId = ?2")
	public void updateToBeRepairedBySensorId(boolean toBeRepaired, Long sensorId);
	
	@Modifying
	@Query("update MaintainersRegistry s set s.toBeCharged = ?1 where fkSensorId = ?2")
	public void updateToBeChargedBySensorId(boolean toBeRepaired, Long sensorId);

	@Modifying
	@Query("update MaintainersRegistry s set s.isUpdating = ?1 where id = ?2")
	public void updateIsUpdatingById(boolean isUpdating, Long id);

//	@Modifying
//	@Query("update MaintainersRegistry s set s.ownerName = ?1 where fkSensorId = ?2")
//	void updateNameBySensorId(String name, Long sensorId);
//	
//	@Modifying
//	@Query("update MaintainersRegistry s set s.ownerSurname = ?1 where fkSensorId = ?2")
//	void updateSurnameBySensorId(String name, Long sensorId);
//	
//	@Modifying
//	@Query("update MaintainersRegistry s set s.company = ?1 where fkSensorId = ?2")
//	void updateCompanyBySensorId(String name, Long sensorId);
//	
//	@Modifying
//	@Query("update MaintainersRegistry s set s.phoneNumber = ?1 where fkSensorId = ?2")
//	void updatePhoneNumberBySensorId(String name, Long sensorId);
//
//	@Modifying
//	@Query("update MaintainersRegistry s set s.mail = ?1 where fkSensorId = ?2")
//	void updateMailBySensorId(String mail, Long sensorId);
	
	@Modifying
	@Query("delete MaintainersRegistry s where fkSensorId = ?1")
	public void deleteSensorMaintainersBySensorId(Long sensorId);
	
	@Modifying
	@Query("delete MaintainersRegistry s where id = ?1")
	public void deleteSensorMaintainersById(Long id);
	
}
