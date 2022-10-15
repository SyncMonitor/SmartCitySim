package it.synclab.smartparking.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.synclab.smartparking.repository.model.Humidity;

@Transactional
@Repository
public interface HumidRepository extends CrudRepository<Humidity,Long>{

}
