package com.example.catalog_service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CatalogServiceApplicationTests {

    @Test
    @Disabled("Desactivado: no tenemos BD configurada para levantar el contexto completo en tests")
    void contextLoads() {
    }
}
