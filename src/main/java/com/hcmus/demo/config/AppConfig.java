package com.hcmus.demo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for application-wide beans.
 * This class is used to define beans that will be managed by the Spring container.
 */
@Configuration
public class AppConfig {

    /**
     * Creates a ModelMapper bean to be used for object mapping.
     * ModelMapper is a library that simplifies the process of mapping objects to each other.
     *
     * @return a new instance of ModelMapper
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
