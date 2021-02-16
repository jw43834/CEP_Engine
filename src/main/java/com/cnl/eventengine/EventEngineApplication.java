package com.cnl.eventengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class EventEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventEngineApplication.class, args);
    }
}
