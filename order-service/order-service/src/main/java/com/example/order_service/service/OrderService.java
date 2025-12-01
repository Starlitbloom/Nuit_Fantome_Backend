package com.example.order_service.service;

import com.example.order_service.dto.CheckoutItemDto;
import com.example.order_service.dto.CheckoutRequest;
import com.example.order_service.dto.CheckoutResponse;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderItem;
import com.example.order_service.model.OrderStatus;
import com.example.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío");
        }

        // -------- 1) Calcular total --------
        BigDecimal total = request.getItems().stream()
                .map(i -> i.getPrecioUnitario()
                        .multiply(BigDecimal.valueOf(i.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // -------- 2) Crear objeto Order --------
        Order order = new Order();
        order.setFechaCreacion(LocalDateTime.now());
        order.setEstado(OrderStatus.PAGADA); // o CREADA si más adelante simulas pago
        order.setTotal(total);

        order.setNombreCliente(request.getNombreCliente());
        order.setEmailCliente(request.getEmailCliente());
        order.setDireccionEnvio(request.getDireccionEnvio());
        order.setRegion(request.getRegion());
        order.setComuna(request.getComuna());
        order.setMetodoEnvio(request.getMetodoEnvio());
        order.setMetodoPago(request.getMetodoPago());

        // -------- 3) Convertir items del carrito en OrderItem --------
        List<OrderItem> items = request.getItems().stream()
                .map(dto -> OrderItem.builder()
                        .productId(dto.getProductId())
                        .nombreProducto(dto.getNombreProducto())
                        .cantidad(dto.getCantidad())
                        .precioUnitario(dto.getPrecioUnitario())
                        .order(order) // relación importante
                        .build())
                .toList();

        order.setItems(items);

        // -------- 4) Guardar en BD --------
        Order savedOrder = orderRepository.save(order);

        // Más adelante:
        // TODO: llamar a inventory-service
        // TODO: llamar a analytics-service

        // -------- 5) Respuesta --------
        return new CheckoutResponse(
                savedOrder.getId(),
                savedOrder.getTotal(),
                savedOrder.getEstado().name(),
                request.getItems()
        );
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findByEmailCliente(email);
    }

    public Order updateStatus(Long id, OrderStatus status) {
        Order order = getOrderById(id);
        order.setEstado(status);
        return orderRepository.save(order);
    }
}
