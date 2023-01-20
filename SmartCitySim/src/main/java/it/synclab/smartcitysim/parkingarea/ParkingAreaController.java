package it.synclab.smartcitysim.parkingarea;

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
import it.synclab.smartcitysim.parkingarea.dtos.ParkingAreaDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/parking-areas")
public class ParkingAreaController{

    @Autowired
    protected ParkingAreaService service;

    @GetMapping()
    @ResponseBody
    public List<ParkingAreaDto> getAll() throws NoHandlerFoundException {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ParkingAreaDto getById(@PathVariable Long id) throws NoHandlerFoundException {
        return service.getById(id);
    }

    @PostMapping()
    @ResponseBody
    public ParkingAreaDto create(@Valid @RequestBody ParkingAreaDto parkingAreaDto) throws NoHandlerFoundException {
        return this.service.create(parkingAreaDto);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ParkingAreaDto update(@PathVariable Long id, @Valid @RequestBody ParkingAreaDto parkingAreaDto) throws NoHandlerFoundException {

        parkingAreaDto.setId(id);

        return this.service.update(parkingAreaDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws NoHandlerFoundException{
        this.service.delete(id);
    }
}
