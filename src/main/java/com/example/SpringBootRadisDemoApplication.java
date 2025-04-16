package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main application class for the Spring Boot Radis Demo.
 * Enables Spring Boot auto-configuration and component scanning.
 * Enables Spring's caching mechanism.
 */
@SpringBootApplication
@EnableCaching 
public class SpringBootRadisDemoApplication { 

    public static void main(String[] args) {
        // Bootstrap and launch the Spring application
        SpringApplication.run(SpringBootRadisDemoApplication.class, args); 
    }

}
