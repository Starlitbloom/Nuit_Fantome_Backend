package com.example.catalog_service.repository;

import com.example.catalog_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Ofertas activas: precioOferta no null y activo = true
    List<Product> findByActivoTrueAndPrecioOfertaIsNotNull();

    @Query("SELECT p FROM Product p " +
           "WHERE p.activo = true " +
           "AND (:categoryId IS NULL OR p.categoria.id = :categoryId) " +
           "AND (" +
           "  :search IS NULL " +
           "  OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "  OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :search, '%'))" +
           ")")
    List<Product> searchProducts(
            @Param("categoryId") Long categoryId,
            @Param("search") String search
    );
}
