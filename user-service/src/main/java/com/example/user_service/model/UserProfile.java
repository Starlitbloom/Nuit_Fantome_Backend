package com.example.user_service.model;

import java.time.LocalDate;

import com.example.user_service.validation.ValidComuna;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ValidComuna
@Data
@Table(name = "perfiles_usuario")
@Entity
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "Perfil de usuario con datos personales, contacto y preferencias")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del perfil", example = "1")
    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Schema(description = "ID del usuario asociado", example = "1")
    private Long userId;

    @NotBlank(message = "El RUT es obligatorio")
    @Size(min = 7, max = 9, message = "El RUT debe tener entre 7 y 9 caracteres")
    @Pattern(regexp = "^[0-9]{7,8}[0-9Kk]$", message = "RUT inválido, solo números y K/k sin puntos ni guion")
    @Schema(description = "RUT del usuario, sin puntos ni guion", example = "12345678K")
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres")
    private String apellido;

    @Pattern(regexp="^\\d{8,9}$", message="Teléfono inválido")
    @Size(min=8, max=11, message="El teléfono debe tener entre 8 y 11 dígitos")
    private String telefono;

    @Past(message = "La fecha de nacimiento debe ser una fecha pasada") // Validación para que la fecha de nacimiento sea en el pasado
    @Schema(description = "Fecha de nacimiento del usuario", example = "2006-01-15")
    private LocalDate fechaNacimiento; // Fecha de nacimiento del usuario, debe ser una fecha pasada

    @NotNull(message = "La región es obligatoria")
    @Enumerated(EnumType.STRING)
    private Region region;

    @NotBlank(message = "La comuna no puede estar vacía")
    private String comuna;
    
    @Column(nullable = false, length =  300) // Longitud máxima de la dirección
    @Size(max = 300, message = "La dirección no puede exceder los 300 caracteres")
    @NotBlank(message = "La dirección no puede estar vacía") // Validación para que la dirección no esté vacía
    @Schema(description = "Dirección del usuario", example = "Volcan Villarrica 429")
    private String direccion; // Dirección del usuario

}