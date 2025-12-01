package com.example.user_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nuit Fantome UserProfile Service API")
                        .version("1.0")
                        .description("Microservicio para gestión de perfiles de usuario de Nuit Fantome"));
    }
}