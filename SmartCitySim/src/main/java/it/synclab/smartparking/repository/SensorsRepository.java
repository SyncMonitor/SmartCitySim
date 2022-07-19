package it.synclab.smartparking.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.synclab.smartparking.repository.model.Sensors;

@Repository
public interface SensorsRepository extends CrudRepository<Sensors, Long> {

}
