package com.example.order_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // opcional (si un día tienes auth)
    private String userId;

    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    private OrderStatus estado;

    private BigDecimal total;

    // Datos del cliente / envío / pago
    private String nombreCliente;
    private String emailCliente;
    private String direccionEnvio;
    private String region;
    private String comuna;
    private String metodoEnvio;
    private String metodoPago;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    // Helpers (opcional) para mantener bien la relación bidireccional
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }
}
