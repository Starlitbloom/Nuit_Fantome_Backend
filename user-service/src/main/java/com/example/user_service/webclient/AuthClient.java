package com.example.user_service.webclient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuthClient {

    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(AuthClient.class);

    public AuthClient(@Value("${auth.service.url}") String authServiceUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(authServiceUrl)
            .build();
    }

    // Obtener ID del usuario por email (endpoint interno)
    public Long getUserIdByEmail(String email, String token) {
        try {
            Map<String, Long> response = webClient.get()
                .uri("/users/internal/email/{email}", email)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Long>>() {})
                .block();

            return response != null ? response.get("id") : null;

        } catch (WebClientResponseException.NotFound e) {
            // Manejo de usuario no encontrado
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con AuthService", e);
        }
    }

    public Map<String, Object> obtenerUsuarioPorId(Long userId, String token) {
        String uri = "/users/" + userId; // Guardamos la URI
        try {
            return webClient.get()
                .uri(uri)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
                .block();
        } catch (WebClientResponseException e) {
            logger.error("Error en AuthClient: status {}, body {}, uri {}", 
                e.getStatusCode().value(), e.getResponseBodyAsString(), uri);
            throw e;
        } catch (Exception e) {
            logger.error("Error inesperado en AuthClient", e);
            throw e;
        }
    }

    public void eliminarUsuario(Long userId) {
        webClient.delete()
            .uri("/auth/internal/user/" + userId)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
