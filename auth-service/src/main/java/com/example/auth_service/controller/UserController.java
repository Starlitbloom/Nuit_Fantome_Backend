package com.example.auth_service.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.auth_service.dto.LoginRequest;
import com.example.auth_service.dto.LoginResponse;
import com.example.auth_service.dto.RegisterRequest;
import com.example.auth_service.model.User;
import com.example.auth_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.example.auth_service.config.JwtUtil;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    // Endpoint para registro
    @Operation(summary = "Registro de usuario", description = "Crea un nuevo usuario y devuelve un JWT")
    @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente",
                content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos o email ya registrado")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegisterRequest request) {

        User user = userService.register(request);

        return ResponseEntity.status(201).body(
            Map.of(
                "message", "Usuario registrado",
                "userId", user.getId(),
                "email", user.getEmail(),
                "role", user.getRole()
            )
        );
    }

    // Endpoint para ingreso
    @Operation(summary = "Ingreso de usuario", description = "Autentica un usuario y devuelve un JWT")
    @ApiResponse(responseCode = "200", description = "Ingreso exitoso",
                content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest request) {

        User user = userService.login(request);

        // Cargar el usuario REAL desde la base de datos
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtUtil.generateToken(userDetails);

        // Borrar la password solo antes de responder
        LoginResponse response = new LoginResponse(
            token,
            user.getId(),
            user.getEmail(),
            user.getRole()
        );

        return ResponseEntity.ok(response);
    }


    //   RESPONSE
    record AuthResponse(String message, String token) {}
}