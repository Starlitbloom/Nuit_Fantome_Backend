package com.example.catalog_service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogServiceApplication.class, args);
    }
}
//Eureka:http://localhost:8761
//Swagger: http://localhost:8081/swagger-ui/index.html
//Actuator: http://localhost:8081/actuator/health