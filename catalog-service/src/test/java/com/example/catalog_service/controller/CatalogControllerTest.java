package com.example.catalog_service.controller;

import com.example.catalog_service.dto.CategoryDto;
import com.example.catalog_service.dto.CategoryRequest;
import com.example.catalog_service.dto.ProductDto;
import com.example.catalog_service.dto.ProductRequest;
import com.example.catalog_service.service.CategoryService;
import com.example.catalog_service.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CatalogController catalogController;

    @Test
    void getCategories_devuelveLista200() {
        CategoryDto dto = CategoryDto.builder()
                .id(1L)
                .nombre("Organizadores")
                .build();

        when(categoryService.getActiveCategories()).thenReturn(List.of(dto));

        ResponseEntity<List<CategoryDto>> response = catalogController.getCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(categoryService).getActiveCategories();
    }

    @Test
    void getProducts_devuelveListaFiltrada() {
        ProductDto dto = ProductDto.builder()
                .id(10L)
                .nombre("Washi")
                .precioNormal(BigDecimal.valueOf(3990))
                .build();

        when(productService.getProducts(1L, "washi")).thenReturn(List.of(dto));

        ResponseEntity<List<ProductDto>> response = catalogController.getProducts(1L, "washi");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(productService).getProducts(1L, "washi");
    }

    @Test
    void getProduct_devuelveDetalle() {
        ProductDto dto = ProductDto.builder()
                .id(5L)
                .nombre("Producto X")
                .build();

        when(productService.getById(5L)).thenReturn(dto);

        ResponseEntity<ProductDto> response = catalogController.getProduct(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Producto X", response.getBody().getNombre());
        verify(productService).getById(5L);
    }

    @Test
    void getOffers_devuelveOfertas() {
        when(productService.getOffers()).thenReturn(List.of(new ProductDto()));

        ResponseEntity<List<ProductDto>> response = catalogController.getOffers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(productService).getOffers();
    }

    @Test
    void createCategory_devuelve201() {
        CategoryRequest request = new CategoryRequest();
        request.setNombre("Nueva");

        CategoryDto dto = CategoryDto.builder()
                .id(1L)
                .nombre("Nueva")
                .build();

        when(categoryService.create(request)).thenReturn(dto);

        ResponseEntity<CategoryDto> response = catalogController.createCategory(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Nueva", response.getBody().getNombre());
        verify(categoryService).create(request);
    }

    @Test
    void updateCategory_devuelve200() {
        CategoryRequest request = new CategoryRequest();
        request.setNombre("Editada");

        CategoryDto dto = CategoryDto.builder()
                .id(1L)
                .nombre("Editada")
                .build();

        when(categoryService.update(1L, request)).thenReturn(dto);

        ResponseEntity<CategoryDto> response = catalogController.updateCategory(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Editada", response.getBody().getNombre());
        verify(categoryService).update(1L, request);
    }

    @Test
    void deleteCategory_devuelve204() {
        ResponseEntity<Void> response = catalogController.deleteCategory(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(categoryService).delete(1L);
    }

    @Test
    void createProduct_devuelve201() {
        ProductRequest request = new ProductRequest();
        request.setNombre("Nuevo producto");

        ProductDto dto = ProductDto.builder()
                .id(1L)
                .nombre("Nuevo producto")
                .build();

        when(productService.create(request)).thenReturn(dto);

        ResponseEntity<ProductDto> response = catalogController.createProduct(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Nuevo producto", response.getBody().getNombre());
        verify(productService).create(request);
    }

    @Test
    void updateProduct_devuelve200() {
        ProductRequest request = new ProductRequest();
        request.setNombre("Editado");

        ProductDto dto = ProductDto.builder()
                .id(1L)
                .nombre("Editado")
                .build();

        when(productService.update(1L, request)).thenReturn(dto);

        ResponseEntity<ProductDto> response = catalogController.updateProduct(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Editado", response.getBody().getNombre());
        verify(productService).update(1L, request);
    }

    @Test
    void deleteProduct_devuelve204() {
        ResponseEntity<Void> response = catalogController.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService).delete(1L);
    }
}
