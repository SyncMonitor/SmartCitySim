package it.synclab.smartparking.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.synclab.smartparking.repository.model.Pm10;

@Transactional
@Repository
public interface Pm10Repository extends CrudRepository<Pm10,Long>{

}
