package com.example.catalog_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    private String nombre;
    private String descripcion;
    private BigDecimal precioNormal;
    private BigDecimal precioOferta;
    private Long categoriaId;
    private String imagenUrl;
    private Boolean activo;
}
