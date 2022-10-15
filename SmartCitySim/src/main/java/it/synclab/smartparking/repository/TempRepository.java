package it.synclab.smartparking.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.synclab.smartparking.repository.model.Temperature;

@Transactional
@Repository
public interface TempRepository extends CrudRepository<Temperature,Long>{

}
