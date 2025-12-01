package com.example.analytics_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SalesDailyDto {

    private LocalDate fecha;
    private Integer totalPedidos;
    private BigDecimal totalVentas;
}
