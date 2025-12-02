package com.example.order_service.controller;

import com.example.order_service.dto.CheckoutRequest;
import com.example.order_service.dto.CheckoutResponse;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderStatus;
import com.example.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")   // 👈 PREFIJO CLARO
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    // ---------- Endpoints CLIENTE ----------

    // Checkout del carrito
    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(@RequestBody CheckoutRequest request) {
        CheckoutResponse response = orderService.checkout(request);
        return ResponseEntity.ok(response);
    }

    // Ver detalle de un pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // Ver pedidos por correo
    @GetMapping("/email/{email}")
    public ResponseEntity<List<Order>> getOrdersByEmail(@PathVariable String email) {
        return ResponseEntity.ok(orderService.getOrdersByEmail(email));
    }

    // ---------- Endpoints ADMIN ----------

    // Lista de todos los pedidos
    @GetMapping("/admin")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // Detalle admin de un pedido
    @GetMapping("/admin/{id}")
    public ResponseEntity<Order> getAdminOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // Cambiar estado de un pedido
    @PutMapping("/admin/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id,
                                              @RequestParam("status") OrderStatus status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }
}
