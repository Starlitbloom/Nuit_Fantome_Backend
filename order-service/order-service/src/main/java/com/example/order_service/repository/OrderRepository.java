package com.example.order_service.repository;

import com.example.order_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Para que un cliente pueda ver sus pedidos usando el correo
    List<Order> findByEmailCliente(String emailCliente);
}
