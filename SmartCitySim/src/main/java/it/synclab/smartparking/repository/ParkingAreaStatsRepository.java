package it.synclab.smartparking.repository;

import org.springframework.data.repository.CrudRepository;

import it.synclab.smartparking.repository.model.ParkingAreaStats;

public interface ParkingAreaStatsRepository extends CrudRepository<ParkingAreaStats, Long> {

}
