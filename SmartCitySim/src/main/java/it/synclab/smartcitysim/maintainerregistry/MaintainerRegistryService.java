package it.synclab.smartcitysim.maintainerregistry;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.synclab.smartcitysim.exception.NotFoundException;
import it.synclab.smartcitysim.maintainerregistry.dtos.MaintainerRegistryDto;
import it.synclab.smartcitysim.maintainerregistry.entities.MaintainerRegistry;
import it.synclab.smartcitysim.mapstruct.mappers.MapStructMapper;

@Service
public class MaintainerRegistryService{
    
    @Autowired
    protected MaintainerRegistryRepository repository;

    @Autowired
    protected MapStructMapper mapStructMapper;

    public List<MaintainerRegistryDto> getAll(){
        List<MaintainerRegistry> maintainersRegistry = repository.findAll();

        List<MaintainerRegistryDto> maintainersRegistryDto = mapStructMapper.maintainersRegistryToMaintainersRegistryDto(maintainersRegistry);

        return maintainersRegistryDto;
    }

    public MaintainerRegistryDto getById(Long id) throws NotFoundException{
        Optional<MaintainerRegistry> maintainerRegistry = repository.findById(id);
        
        if(!maintainerRegistry.isPresent()){
            throw new NotFoundException("Object id not found");
        }

        MaintainerRegistryDto maintainerRegistryDto = mapStructMapper.maintainerRegistryToMaintainerRegistryDto(maintainerRegistry.get());

        return maintainerRegistryDto;
    }

    public MaintainerRegistryDto create(MaintainerRegistryDto maintainerRegistryDto){
        MaintainerRegistry maintainerRegistry = mapStructMapper.maintainerRegistryDtoToMaintainerRegistry(maintainerRegistryDto);

        MaintainerRegistry maintainerRegistrySaved = repository.save(maintainerRegistry);
        MaintainerRegistryDto maintainerRegistryDtoSaved = mapStructMapper.maintainerRegistryToMaintainerRegistryDto(maintainerRegistrySaved);

        return maintainerRegistryDtoSaved;
    }

    public MaintainerRegistryDto update(MaintainerRegistryDto maintainerRegistryDto) throws NotFoundException{
        Long id = maintainerRegistryDto.getId();

        getById(id);

        MaintainerRegistry maintainerRegistry = mapStructMapper.maintainerRegistryDtoToMaintainerRegistry(maintainerRegistryDto);
        MaintainerRegistry maintainerRegistrySaved = repository.save(maintainerRegistry);
        MaintainerRegistryDto maintainerRegistryDtoSaved = mapStructMapper.maintainerRegistryToMaintainerRegistryDto(maintainerRegistrySaved);

        return maintainerRegistryDtoSaved;
    }

    public void delete(Long id) throws NotFoundException {
        getById(id);

        repository.deleteById(id);
    }
}
