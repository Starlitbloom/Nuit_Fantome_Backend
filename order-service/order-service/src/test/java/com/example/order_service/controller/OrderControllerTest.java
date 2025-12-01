package com.example.order_service.controller;

import com.example.order_service.dto.CheckoutRequest;
import com.example.order_service.dto.CheckoutResponse;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderStatus;
import com.example.order_service.service.OrderService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    void checkout_DevuelveResponseCorrecto() {
        CheckoutRequest req = new CheckoutRequest();

        CheckoutResponse fakeResponse = new CheckoutResponse(
                10L,
                BigDecimal.valueOf(19990),
                "PAGADA",
                List.of()
        );

        when(orderService.checkout(any())).thenReturn(fakeResponse);

        CheckoutResponse response = orderController.checkout(req).getBody();

        assertNotNull(response);
        assertEquals(10L, response.getOrderId());
        assertEquals("PAGADA", response.getEstado());
    }

    @Test
    void getOrderById_DevuelvePedido() {
        Order fakeOrder = new Order();
        fakeOrder.setId(5L);

        when(orderService.getOrderById(5L)).thenReturn(fakeOrder);

        Order result = orderController.getOrder(5L).getBody();

        assertNotNull(result);
        assertEquals(5L, result.getId());
    }

    @Test
    void updateStatus_CambiaEstado() {
        Order fakeOrder = new Order();
        fakeOrder.setId(7L);
        fakeOrder.setEstado(OrderStatus.DESPACHADA);

        when(orderService.updateStatus(7L, OrderStatus.DESPACHADA))
                .thenReturn(fakeOrder);

        Order result = orderController.updateStatus(7L, OrderStatus.DESPACHADA)
                .getBody();

        assertNotNull(result);
        assertEquals(OrderStatus.DESPACHADA, result.getEstado());
    }
}
