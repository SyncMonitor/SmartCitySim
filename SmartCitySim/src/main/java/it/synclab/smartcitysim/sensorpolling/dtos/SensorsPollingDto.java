package it.synclab.smartcitysim.sensorpolling.dtos;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JacksonXmlRootElement(localName = "markers")
public class SensorsPollingDto {

    @JacksonXmlProperty(localName = "marker")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<SensorPollingDto> sensorsPollingDto;
}
