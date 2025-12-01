package com.example.catalog_service.dto;

import lombok.Data;

@Data
public class CategoryRequest {

    private String nombre;
    private String descripcion;
    private Boolean activa;
}
