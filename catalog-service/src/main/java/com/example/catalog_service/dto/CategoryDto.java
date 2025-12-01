package com.example.catalog_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private boolean activa;
}
