package com.example.order_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class CheckoutRequest {

    // Datos del cliente
    private String nombreCliente;
    private String emailCliente;
    private String direccionEnvio;
    private String region;
    private String comuna;
    private String metodoEnvio;
    private String metodoPago;

    // Items del carrito
    private List<CheckoutItemDto> items;
}
