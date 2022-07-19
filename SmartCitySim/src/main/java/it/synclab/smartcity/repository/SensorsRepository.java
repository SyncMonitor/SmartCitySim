package it.synclab.smartcity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.synclab.smartparking.repository.model.Sensors;

@Repository
public interface SensorsRepository extends JpaRepository<Sensors, Long> {

}
