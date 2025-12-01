package com.example.analytics_service.repository;

import com.example.analytics_service.model.ProductSales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductSalesRepository extends JpaRepository<ProductSales, Long> {

    List<ProductSales> findByFechaBetween(LocalDate from, LocalDate to);
}
