package com.example.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.user_service.model.UserProfile;
import com.example.user_service.service.UserProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/v1/user-profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService service) {
        this.userProfileService = service;
    }

    // ========================= CREAR PERFIL =========================
    @Operation(
        summary = "Crear un perfil de usuario",
        description = "Crea un nuevo perfil. Los usuarios CLIENTE solo pueden crear su propio perfil. ADMIN puede crear cualquiera."
    )
    @ApiResponse(responseCode = "201", description = "Perfil creado exitosamente",
        content = @Content(schema = @Schema(implementation = UserProfile.class)))
    @PostMapping("/")
    public ResponseEntity<UserProfile> crearPerfil(
            @RequestBody UserProfile perfil,
            Authentication auth) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userProfileService.crearPerfil(perfil, auth));
    }

    // ========================= OBTENER PERFIL =========================
    @Operation(
        summary = "Obtener un perfil de usuario",
        description = "Devuelve la información del perfil. ADMIN puede ver cualquiera. CLIENTE o VENDEDOR solo el suyo."
    )
    @ApiResponse(responseCode = "200", description = "Perfil encontrado",
        content = @Content(schema = @Schema(implementation = UserProfile.class)))
    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> obtenerPerfil(
            @PathVariable Long id,
            Authentication auth) {

        String role = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        String email = auth.getName();

        // 🔑 Obtener requesterId real desde AuthService
        Long requesterId = userProfileService.getUserIdFromEmail(email);

        UserProfile perfil = userProfileService.obtenerPerfil(requesterId, id, role);

        return ResponseEntity.ok(perfil);
    }

    // ========================= ACTUALIZAR PERFIL =========================
    @Operation(
        summary = "Actualizar un perfil de usuario",
        description = "Actualiza los datos del perfil. ADMIN puede editar cualquier perfil; los usuarios solo el suyo."
    )
    @ApiResponse(responseCode = "200", description = "Perfil actualizado",
        content = @Content(schema = @Schema(implementation = UserProfile.class)))
    @PutMapping("/{id}")
    public ResponseEntity<UserProfile> actualizarPerfil(
            @PathVariable Long id,
            @RequestBody UserProfile datos,
            Authentication auth) {

        String role = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        String email = auth.getName();

        // 🔑 Obtener requesterId real desde AuthService
        Long requesterId = userProfileService.getUserIdFromEmail(email);

        UserProfile actualizado = userProfileService.actualizarPerfilPorId(requesterId, id, datos, role);

        return ResponseEntity.ok(actualizado);
    }

    // ========================= ELIMINAR PERFIL =========================
    @Operation(
        summary = "Eliminar un perfil de usuario",
        description = "Elimina el perfil. Solo ADMIN puede eliminarlo."
    )
    @ApiResponse(responseCode = "204", description = "Perfil eliminado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPerfil(
            @PathVariable Long id,
            Authentication auth) {

        // Solo ADMIN puede eliminar (la validación ya está en el service)
        userProfileService.eliminarPerfil(id);

        return ResponseEntity.noContent().build();
    }

}
