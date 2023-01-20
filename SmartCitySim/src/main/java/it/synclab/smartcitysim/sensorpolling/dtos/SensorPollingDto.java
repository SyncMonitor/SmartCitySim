package it.synclab.smartcitysim.sensorpolling.dtos;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorPollingDto {
    private Long id;

    private String name;

    private String address;

    @JacksonXmlProperty(localName = "lat")
    private String latitude;

    @JacksonXmlProperty(localName = "lng")
    private String longitude;

    private Boolean state;
    
    private String battery;

    private String charge;

    private Boolean active;

    private String type;

    public SensorPollingDto(){
        this.type = "ParkingSensor";
    }

    public void setState(String state){
        if(state.equals("0")) this.state = false;
        else this.state = true;
    }

    public void setActive(String active){
        if(active.equals("0")) this.active = false;
        else this.active = true;
    }

    public void setBattery(String battery){
        this.battery = battery;
        this.charge = String.valueOf(
                (int)Math.floor(
                    Float.valueOf(
                        battery.replace(',', '.').replaceAll("[^\\d.]", "")
                )
            )
        );
    }
}
