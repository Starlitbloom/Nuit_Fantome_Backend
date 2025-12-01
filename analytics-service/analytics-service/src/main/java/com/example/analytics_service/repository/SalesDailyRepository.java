package com.example.analytics_service.repository;

import com.example.analytics_service.model.SalesDaily;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SalesDailyRepository extends JpaRepository<SalesDaily, LocalDate> {

    List<SalesDaily> findByFechaBetween(LocalDate from, LocalDate to);
}
