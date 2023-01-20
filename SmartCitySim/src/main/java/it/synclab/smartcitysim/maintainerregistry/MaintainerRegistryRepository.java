package it.synclab.smartcitysim.maintainerregistry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.synclab.smartcitysim.maintainerregistry.entities.MaintainerRegistry;

@Repository
public interface MaintainerRegistryRepository extends CrudRepository<MaintainerRegistry, Long> {
    List<MaintainerRegistry> findAll();
    Optional<MaintainerRegistry> findById(Long id);
}
