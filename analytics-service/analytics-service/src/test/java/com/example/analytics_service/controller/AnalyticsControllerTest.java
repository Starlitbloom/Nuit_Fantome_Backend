package com.example.analytics_service.controller;

import com.example.analytics_service.dto.OrderPaidEventDto;
import com.example.analytics_service.dto.SalesDailyDto;
import com.example.analytics_service.dto.TopProductDto;
import com.example.analytics_service.service.AnalyticsService;

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
class AnalyticsControllerTest {

    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks
    private AnalyticsController analyticsController;

    @Test
    void registerOrderPaid_respondeOK() {
        OrderPaidEventDto event = new OrderPaidEventDto();

        doNothing().when(analyticsService).registerOrderPaid(any());

        var response = analyticsController.registerOrderPaid(event);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void getSalesDaily_devuelveLista() {
        LocalDate from = LocalDate.of(2025, 1, 1);
        LocalDate to   = LocalDate.of(2025, 1, 31);

        List<SalesDailyDto> fakeList = List.of(
                new SalesDailyDto(from, 10, BigDecimal.valueOf(50000))
        );

        when(analyticsService.getSalesDaily(from, to)).thenReturn(fakeList);

        var response = analyticsController.getSalesDaily(from, to);

        assertEquals(1, response.getBody().size());
        assertEquals(10, response.getBody().get(0).getTotalPedidos());
    }

    @Test
    void getTopProducts_devuelveListaOrdenada() {
        LocalDate from = LocalDate.of(2025, 2, 1);
        LocalDate to   = LocalDate.of(2025, 2, 28);

        List<TopProductDto> fakeList = List.of(
                new TopProductDto(1L, 10L, BigDecimal.valueOf(30000))
        );

        when(analyticsService.getTopProducts(from, to, 5))
                .thenReturn(fakeList);

        var response = analyticsController.getTopProducts(from, to, 5);

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getProductId());
    }
}
