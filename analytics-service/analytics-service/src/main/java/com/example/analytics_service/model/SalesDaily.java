package com.example.analytics_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sales_daily")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesDaily {

    @Id
    private LocalDate fecha;   // PK: una fila por día

    private Integer totalPedidos;

    private BigDecimal totalVentas;
}
