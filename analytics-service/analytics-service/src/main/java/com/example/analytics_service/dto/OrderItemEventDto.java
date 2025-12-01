package com.example.analytics_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemEventDto {
    private Long productId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}
