package com.example.analytics_service.service;

import com.example.analytics_service.dto.OrderItemEventDto;
import com.example.analytics_service.dto.OrderPaidEventDto;
import com.example.analytics_service.dto.SalesDailyDto;
import com.example.analytics_service.dto.TopProductDto;
import com.example.analytics_service.model.ProductSales;
import com.example.analytics_service.model.SalesDaily;
import com.example.analytics_service.repository.ProductSalesRepository;
import com.example.analytics_service.repository.SalesDailyRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private SalesDailyRepository salesDailyRepository;

    @Mock
    private ProductSalesRepository productSalesRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    private OrderItemEventDto item(long productId, int cantidad, int precio) {
        OrderItemEventDto dto = new OrderItemEventDto();
        dto.setProductId(productId);
        dto.setCantidad(cantidad);
        dto.setPrecioUnitario(BigDecimal.valueOf(precio));
        return dto;
    }

    @Test
    void registerOrderPaid_actualizaSalesDaily() {
        // Arrange
        LocalDate fecha = LocalDate.now();

        OrderPaidEventDto event = new OrderPaidEventDto();
        event.setOrderId(1L);
        event.setFecha(fecha);
        event.setTotal(BigDecimal.valueOf(10000));
        event.setItems(List.of(item(1L, 1, 10000)));

        SalesDaily diario = new SalesDaily(fecha, 0, BigDecimal.ZERO);

        when(salesDailyRepository.findById(fecha)).thenReturn(java.util.Optional.of(diario));

        // Act
        analyticsService.registerOrderPaid(event);

        // Assert
        assertEquals(1, diario.getTotalPedidos());
        assertEquals(BigDecimal.valueOf(10000), diario.getTotalVentas());

        verify(salesDailyRepository, times(1)).save(diario);
        verify(productSalesRepository, times(1)).save(any(ProductSales.class));
    }

    @Test
    void getSalesDaily_devuelveListaCorrecta() {
        LocalDate from = LocalDate.of(2025, 1, 1);
        LocalDate to   = LocalDate.of(2025, 1, 31);

        SalesDaily sd = new SalesDaily(from, 5, BigDecimal.valueOf(50000));

        when(salesDailyRepository.findByFechaBetween(from, to))
                .thenReturn(List.of(sd));

        List<SalesDailyDto> result = analyticsService.getSalesDaily(from, to);

        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getTotalPedidos());
    }

    @Test
    void getTopProducts_devuelveTopOrdenado() {
        LocalDate from = LocalDate.of(2025, 1, 1);
        LocalDate to   = LocalDate.of(2025, 1, 31);

        ProductSales p1 = new ProductSales(1L, 1L, from, 5, BigDecimal.valueOf(5000));
        ProductSales p2 = new ProductSales(2L, 1L, from, 10, BigDecimal.valueOf(10000));

        when(productSalesRepository.findByFechaBetween(from, to))
                .thenReturn(List.of(p1, p2));

        List<TopProductDto> result = analyticsService.getTopProducts(from, to, 5);

        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).getProductId()); // el que venda más va primero
    }
}
