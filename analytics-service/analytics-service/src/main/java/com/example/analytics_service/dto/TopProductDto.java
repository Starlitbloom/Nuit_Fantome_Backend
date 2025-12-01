package com.example.analytics_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TopProductDto {

    private Long productId;
    private Long cantidadVendida;
    private BigDecimal totalVendido;
}
