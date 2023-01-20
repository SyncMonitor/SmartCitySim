package it.synclab.smartcitysim.parkingarea;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.synclab.smartcitysim.parkingarea.entities.ParkingArea;

@Repository
public interface ParkingAreaRepository extends CrudRepository<ParkingArea, Long>{
    List<ParkingArea> findAll();
    Optional<ParkingArea> findById(Long id);
}
