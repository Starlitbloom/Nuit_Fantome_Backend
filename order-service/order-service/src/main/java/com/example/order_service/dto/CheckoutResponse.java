package com.example.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class CheckoutResponse {

    private Long orderId;
    private BigDecimal total;
    private String estado;
    private List<CheckoutItemDto> items;
}
