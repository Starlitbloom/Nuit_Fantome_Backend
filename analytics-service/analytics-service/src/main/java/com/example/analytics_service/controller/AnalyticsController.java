package com.example.analytics_service.controller;

import com.example.analytics_service.dto.OrderPaidEventDto;
import com.example.analytics_service.dto.SalesDailyDto;
import com.example.analytics_service.dto.TopProductDto;
import com.example.analytics_service.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // Llamado desde order-service cuando un pedido queda pagado
    @PostMapping("/analytics/order-paid")
    public ResponseEntity<Void> registerOrderPaid(@RequestBody OrderPaidEventDto event) {
        analyticsService.registerOrderPaid(event);
        return ResponseEntity.ok().build();
    }

    // Vista admin: ventas diarias
    @GetMapping("/admin/analytics/sales-daily")
    public ResponseEntity<List<SalesDailyDto>> getSalesDaily(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(analyticsService.getSalesDaily(from, to));
    }

    // Vista admin: top productos
    @GetMapping("/admin/analytics/top-products")
    public ResponseEntity<List<TopProductDto>> getTopProducts(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(name = "limit", defaultValue = "5") int limit
    ) {
        return ResponseEntity.ok(analyticsService.getTopProducts(from, to, limit));
    }
}
