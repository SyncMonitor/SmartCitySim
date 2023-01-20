package it.synclab.smartcitysim.sensormaintenance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.synclab.smartcitysim.sensormaintenance.entities.SensorMaintenance;

@Repository
public interface SensorMaintenanceRepository extends CrudRepository<SensorMaintenance, Long> {
    List<SensorMaintenance> findAll();
    Optional<SensorMaintenance> findById(Long id);
}
