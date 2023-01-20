package it.synclab.smartcitysim.maintainerregistry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.synclab.smartcitysim.exception.NoHandlerFoundException;
import it.synclab.smartcitysim.maintainerregistry.dtos.MaintainerRegistryDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/maintainers")
public class MaintainerRegistryController{
    @Autowired
    protected MaintainerRegistryService service;

    @GetMapping()
    @ResponseBody
    public List<MaintainerRegistryDto> getAll() throws NoHandlerFoundException {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public MaintainerRegistryDto getById(@PathVariable Long id) throws NoHandlerFoundException {
        return service.getById(id);
    }

    @PostMapping()
    @ResponseBody
    public MaintainerRegistryDto create(@Valid @RequestBody MaintainerRegistryDto maintainerRegistryDto) throws NoHandlerFoundException {
        return this.service.create(maintainerRegistryDto);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public MaintainerRegistryDto update(@PathVariable Long id, @Valid @RequestBody MaintainerRegistryDto maintainerRegistryDto) throws NoHandlerFoundException {

        maintainerRegistryDto.setId(id);

        return this.service.update(maintainerRegistryDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws NoHandlerFoundException{
        this.service.delete(id);
    }
}
