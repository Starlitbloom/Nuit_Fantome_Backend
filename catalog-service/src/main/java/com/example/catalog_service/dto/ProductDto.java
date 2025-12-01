package com.example.catalog_service.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioNormal;
    private BigDecimal precioOferta;
    private Long categoriaId;
    private String categoriaNombre;
    private String imagenUrl;
    private boolean activo;
}
