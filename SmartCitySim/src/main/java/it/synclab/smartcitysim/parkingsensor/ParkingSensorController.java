package it.synclab.smartcitysim.parkingsensor;

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
import it.synclab.smartcitysim.parkingsensor.dtos.ParkingSensorDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/parking-sensors")
public class ParkingSensorController{
    @Autowired
    protected ParkingSensorService service;

    @GetMapping()
    @ResponseBody
    public List<ParkingSensorDto> getAll() throws NoHandlerFoundException {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ParkingSensorDto getById(@PathVariable Long id) throws NoHandlerFoundException {
        return service.getById(id);
    }

    @PostMapping()
    @ResponseBody
    public ParkingSensorDto create(@Valid @RequestBody ParkingSensorDto parkingSensorDto) throws NoHandlerFoundException {
        return this.service.create(parkingSensorDto);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ParkingSensorDto update(@PathVariable Long id, @Valid @RequestBody ParkingSensorDto parkingSensorDto) throws NoHandlerFoundException {

        parkingSensorDto.setId(id);

        return this.service.update(parkingSensorDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws NoHandlerFoundException{
        this.service.delete(id);
    }
}
