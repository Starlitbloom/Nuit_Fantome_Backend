package com.example.catalog_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioNormal;

    @Column(precision = 10, scale = 2)
    private BigDecimal precioOferta; // null si no está en oferta

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category categoria;

    @Column(length = 255)
    private String imagenUrl;

    @Builder.Default
    @Column(nullable = false)
    private boolean activo = true;
}
