package com.example.analytics_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderPaidEventDto {

    private Long orderId;
    private BigDecimal total;
    private LocalDate fecha;
    private List<OrderItemEventDto> items;
}
