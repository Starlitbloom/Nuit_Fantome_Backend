package com.example.catalog_service.service;

import com.example.catalog_service.dto.ProductDto;
import com.example.catalog_service.dto.ProductRequest;
import com.example.catalog_service.model.Category;
import com.example.catalog_service.model.Product;
import com.example.catalog_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    // Públicos: lista de productos con filtros opcionales
    public List<ProductDto> getProducts(Long categoryId, String search) {
        if (search != null && search.isBlank()) {
            search = null;
        }
        List<Product> products = productRepository.searchProducts(categoryId, search);
        return products.stream().map(this::toDto).toList();
    }

    // Públicos: productos en oferta
    public List<ProductDto> getOffers() {
        return productRepository.findByActivoTrueAndPrecioOfertaIsNotNull()
                .stream()
                .map(this::toDto)
                .toList();
    }

    // Públicos: detalle de producto
    public ProductDto getById(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return toDto(p);
    }

    // Admin: crear producto
    public ProductDto create(ProductRequest request) {
        Category category = categoryService.getEntity(request.getCategoriaId());

        Product p = Product.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precioNormal(request.getPrecioNormal())
                .precioOferta(request.getPrecioOferta())
                .categoria(category)
                .imagenUrl(request.getImagenUrl())
                .activo(request.getActivo() == null ? true : request.getActivo())
                .build();

        return toDto(productRepository.save(p));
    }

    // Admin: actualizar producto
    public ProductDto update(Long id, ProductRequest request) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (request.getNombre() != null) p.setNombre(request.getNombre());
        if (request.getDescripcion() != null) p.setDescripcion(request.getDescripcion());
        if (request.getPrecioNormal() != null) p.setPrecioNormal(request.getPrecioNormal());
        if (request.getPrecioOferta() != null) p.setPrecioOferta(request.getPrecioOferta());
        if (request.getImagenUrl() != null) p.setImagenUrl(request.getImagenUrl());
        if (request.getActivo() != null) p.setActivo(request.getActivo());

        if (request.getCategoriaId() != null) {
            Category category = categoryService.getEntity(request.getCategoriaId());
            p.setCategoria(category);
        }

        return toDto(productRepository.save(p));
    }

    // Admin: eliminar producto
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productRepository.deleteById(id);
    }

    private ProductDto toDto(Product p) {
        return ProductDto.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .precioNormal(p.getPrecioNormal())
                .precioOferta(p.getPrecioOferta())
                .categoriaId(p.getCategoria().getId())
                .categoriaNombre(p.getCategoria().getNombre())
                .imagenUrl(p.getImagenUrl())
                .activo(p.isActivo())
                .build();
    }
}
