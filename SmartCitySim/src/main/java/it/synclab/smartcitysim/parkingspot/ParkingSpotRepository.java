package it.synclab.smartcitysim.parkingspot;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.synclab.smartcitysim.parkingspot.entities.ParkingSpot;

@Repository
public interface ParkingSpotRepository extends CrudRepository<ParkingSpot, Long> {
    List<ParkingSpot> findAll();
    Optional<ParkingSpot> findById(Long id);
    
}
