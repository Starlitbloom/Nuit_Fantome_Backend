package com.example.order_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CheckoutItemDto {

    private Long productId;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}
