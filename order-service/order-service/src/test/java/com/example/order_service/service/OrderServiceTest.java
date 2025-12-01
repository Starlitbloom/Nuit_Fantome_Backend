package com.example.order_service.service;

import com.example.order_service.dto.CheckoutItemDto;
import com.example.order_service.dto.CheckoutRequest;
import com.example.order_service.dto.CheckoutResponse;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderStatus;
import com.example.order_service.repository.OrderRepository;

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
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private CheckoutItemDto buildItem(Long id, int cantidad, int precio) {
        CheckoutItemDto dto = new CheckoutItemDto();
        dto.setProductId(id);
        dto.setNombreProducto("Producto " + id);
        dto.setCantidad(cantidad);
        dto.setPrecioUnitario(BigDecimal.valueOf(precio));
        return dto;
    }

    @Test
    void checkout_CreaOrdenCorrectamente() {
        // Arrange
        CheckoutRequest request = new CheckoutRequest();
        request.setNombreCliente("Sebastián");
        request.setEmailCliente("seba@example.com");
        request.setDireccionEnvio("Ñuñoa");
        request.setRegion("RM");
        request.setComuna("Ñuñoa");
        request.setMetodoEnvio("STARKEN");
        request.setMetodoPago("TRANSFERENCIA");

        request.setItems(List.of(
                buildItem(1L, 2, 3990),
                buildItem(2L, 1, 12990)
        ));

        Order fakeOrder = new Order();
        fakeOrder.setId(1L);
        fakeOrder.setEstado(OrderStatus.PAGADA);
        fakeOrder.setTotal(BigDecimal.valueOf(3990 * 2 + 12990));

        when(orderRepository.save(any(Order.class))).thenReturn(fakeOrder);

        // Act
        CheckoutResponse response = orderService.checkout(request);

        // Assert
        assertEquals(1L, response.getOrderId());
        assertEquals("PAGADA", response.getEstado());
        assertEquals(BigDecimal.valueOf(3990 * 2 + 12990), response.getTotal());

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void checkout_ConCarritoVacio_LanzaError() {
        CheckoutRequest request = new CheckoutRequest();

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> orderService.checkout(request));

        assertEquals("El carrito está vacío", ex.getMessage());
    }
}
