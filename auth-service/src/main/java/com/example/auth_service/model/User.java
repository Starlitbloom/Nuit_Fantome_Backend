package com.example.auth_service.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Correo inválido")
    @NotBlank(message = "El email es obligatorio")
    @Size(max = 100, message = "El correo no puede exceder los 100 caracteres")
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@(duoc\\.cl|profesor\\.duoc\\.cl|gmail\\.com)$",
        message = "Solo se permiten correos @duoc.cl, @profesor.duoc.cl o @gmail.com"
    )
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Contraseña obligatoria")
    @Size(min = 4, message = "La contraseña debe tener minimo 4 caracteres")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol role = Rol.CLIENTE; // Por defecto CLIENTE

    @Column(nullable = false)
    private boolean active = true; // No puede estar vacío

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
