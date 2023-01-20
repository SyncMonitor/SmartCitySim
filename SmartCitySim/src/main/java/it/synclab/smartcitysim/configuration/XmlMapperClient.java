package it.synclab.smartcitysim.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Configuration
public class XmlMapperClient{
    @Bean
    public XmlMapper xmlMapper(){
        return new XmlMapper();
    }
}