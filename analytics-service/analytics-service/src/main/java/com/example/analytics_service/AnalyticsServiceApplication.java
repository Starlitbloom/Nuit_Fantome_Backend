package com.example.analytics_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient   // 👈 NECESARIO para registrarse en Eureka
public class AnalyticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsServiceApplication.class, args);
    }

}
//http://localhost:8084/actuator/health
//En Eureka:(http://localhost:8761
//En Swagge:http://localhost:8084/swagger-ui/index.html