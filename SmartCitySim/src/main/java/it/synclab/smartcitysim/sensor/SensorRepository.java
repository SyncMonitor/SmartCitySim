package it.synclab.smartcitysim.sensor;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.synclab.smartcitysim.sensor.entities.Sensor;

@Repository
public interface SensorRepository extends CrudRepository<Sensor, Long>{
    List<Sensor> findAll();
    Optional<Sensor> findById(Long id);
    @Query("SELECT s FROM Sensor s WHERE s NOT IN (SELECT sm.sensor FROM Sensor s LEFT JOIN SensorMaintenance sm)")
    List<Sensor> getAllWithoutMaintenance();
}
