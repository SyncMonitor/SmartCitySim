package it.synclab.smartparking.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.synclab.smartparking.repository.model.ParkingArea;

@Repository
public interface ParkingAreaRepository extends CrudRepository<ParkingArea, Long> {

}
