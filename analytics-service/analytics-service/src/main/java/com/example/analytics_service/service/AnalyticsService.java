package com.example.analytics_service.service;

import com.example.analytics_service.dto.*;
import com.example.analytics_service.model.ProductSales;
import com.example.analytics_service.model.SalesDaily;
import com.example.analytics_service.repository.ProductSalesRepository;
import com.example.analytics_service.repository.SalesDailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final SalesDailyRepository salesDailyRepository;
    private final ProductSalesRepository productSalesRepository;

    @Transactional
    public void registerOrderPaid(OrderPaidEventDto event) {

        LocalDate fecha = event.getFecha() != null ? event.getFecha() : LocalDate.now();

        // ---- Actualizar SalesDaily ----
        SalesDaily daily = salesDailyRepository.findById(fecha)
                .orElseGet(() -> SalesDaily.builder()
                        .fecha(fecha)
                        .totalPedidos(0)
                        .totalVentas(BigDecimal.ZERO)
                        .build()
                );

        daily.setTotalPedidos(daily.getTotalPedidos() + 1);
        daily.setTotalVentas(daily.getTotalVentas().add(event.getTotal()));

        salesDailyRepository.save(daily);

        // ---- Guardar ProductSales por cada item ----
        for (OrderItemEventDto item : event.getItems()) {
            BigDecimal totalItem = item.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(item.getCantidad()));

            ProductSales ps = ProductSales.builder()
                    .productId(item.getProductId())
                    .fecha(fecha)
                    .cantidadVendida(item.getCantidad())
                    .totalVendido(totalItem)
                    .build();

            productSalesRepository.save(ps);
        }
    }

    public List<SalesDailyDto> getSalesDaily(LocalDate from, LocalDate to) {
        var list = salesDailyRepository.findByFechaBetween(from, to);
        return list.stream()
                .map(d -> new SalesDailyDto(
                        d.getFecha(),
                        d.getTotalPedidos(),
                        d.getTotalVentas()
                ))
                .toList();
    }

    public List<TopProductDto> getTopProducts(LocalDate from, LocalDate to, int limit) {
        var list = productSalesRepository.findByFechaBetween(from, to);

        // Agrupar por productId
        Map<Long, List<ProductSales>> grouped = list.stream()
                .collect(Collectors.groupingBy(ProductSales::getProductId));

        List<TopProductDto> result = new ArrayList<>();

        for (Map.Entry<Long, List<ProductSales>> entry : grouped.entrySet()) {
            Long productId = entry.getKey();

            long cantidadTotal = entry.getValue().stream()
                    .mapToLong(ps -> ps.getCantidadVendida().longValue())
                    .sum();

            BigDecimal totalVendido = entry.getValue().stream()
                    .map(ProductSales::getTotalVendido)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            result.add(new TopProductDto(
                    productId,
                    cantidadTotal,
                    totalVendido
            ));
        }

        // Ordenar por cantidadVendida desc y limitar
        return result.stream()
                .sorted(Comparator.comparingLong(TopProductDto::getCantidadVendida).reversed())
                .limit(limit)
                .toList();
    }
}
