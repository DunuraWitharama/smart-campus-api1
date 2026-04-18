package com.smartcampus.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

public class AppConfig extends ResourceConfig {

    public AppConfig() {

        packages("com.smartcampus");

        
        register(JacksonFeature.class);
    }
}