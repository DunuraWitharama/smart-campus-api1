package com.smartcampus.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.smartcampus.filter.LoggingFilter;

@ApplicationPath("/api/v1") 
public class AppConfig extends ResourceConfig {

    public AppConfig() {

        packages("com.smartcampus");

        register(JacksonFeature.class);
        register(LoggingFilter.class);
    }
}