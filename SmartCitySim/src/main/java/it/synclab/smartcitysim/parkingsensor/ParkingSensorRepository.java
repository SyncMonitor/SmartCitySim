package it.synclab.smartcitysim.parkingsensor;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.synclab.smartcitysim.parkingsensor.entities.ParkingSensor;

@Repository
public interface ParkingSensorRepository extends CrudRepository<ParkingSensor, Long> {
    List<ParkingSensor> findAll();
    Optional<ParkingSensor> findById(Long id);
}
