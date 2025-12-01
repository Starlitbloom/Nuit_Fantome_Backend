package com.example.inventory_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  // 👈 esto hace que se registre en Eureka
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

}
//Swagger (documentación de la API) http://localhost:8082/swagger-ui/index.html
//Ver health http://localhost:8082/actuator/health
//Eureka Server http://localhost:8761

