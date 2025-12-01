package com.example.catalog_service.service;

import com.example.catalog_service.dto.ProductDto;
import com.example.catalog_service.dto.ProductRequest;
import com.example.catalog_service.model.Category;
import com.example.catalog_service.model.Product;
import com.example.catalog_service.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    @Test
    void getProducts_llamaRepositoryConFiltrosYMapeaDtos() {
        Long categoryId = 1L;
        String search = "washi";

        Category cat = Category.builder()
                .id(categoryId)
                .nombre("Cintas")
                .build();

        Product product = Product.builder()
                .id(10L)
                .nombre("Washi rosa")
                .descripcion("Cinta washi rosa")
                .precioNormal(BigDecimal.valueOf(3990))
                .categoria(cat)
                .activo(true)
                .build();

        when(productRepository.searchProducts(categoryId, search))
                .thenReturn(List.of(product));

        List<ProductDto> result = productService.getProducts(categoryId, search);

        assertEquals(1, result.size());
        assertEquals("Washi rosa", result.get(0).getNombre());
        verify(productRepository).searchProducts(categoryId, search);
    }

    @Test
    void getOffers_devuelveSoloOfertas() {
        Category cat = Category.builder()
                .id(1L)
                .nombre("Organizadores")
                .build();

        Product product = Product.builder()
                .id(2L)
                .nombre("Organizador")
                .precioNormal(BigDecimal.valueOf(12990))
                .precioOferta(BigDecimal.valueOf(9990))
                .categoria(cat)
                .activo(true)
                .build();

        when(productRepository.findByActivoTrueAndPrecioOfertaIsNotNull())
                .thenReturn(List.of(product));

        List<ProductDto> result = productService.getOffers();

        assertEquals(1, result.size());
        assertEquals(BigDecimal.valueOf(9990), result.get(0).getPrecioOferta());
        verify(productRepository).findByActivoTrueAndPrecioOfertaIsNotNull();
    }

    @Test
    void create_creaProductoConCategoriaValida() {
        ProductRequest request = new ProductRequest();
        request.setNombre("Nuevo producto");
        request.setDescripcion("Desc");
        request.setPrecioNormal(BigDecimal.valueOf(5000));
        request.setCategoriaId(1L);
        request.setImagenUrl("img.jpg");
        request.setActivo(true);

        Category category = Category.builder()
                .id(1L)
                .nombre("Categoria")
                .build();

        Product saved = Product.builder()
                .id(100L)
                .nombre("Nuevo producto")
                .descripcion("Desc")
                .precioNormal(BigDecimal.valueOf(5000))
                .categoria(category)
                .imagenUrl("img.jpg")
                .activo(true)
                .build();

        when(categoryService.getEntity(1L)).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        ProductDto result = productService.create(request);

        assertNotNull(result.getId());
        assertEquals("Nuevo producto", result.getNombre());
        assertEquals(1L, result.getCategoriaId());
        verify(categoryService).getEntity(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void update_actualizaCamposDelProducto() {
        Long id = 5L;

        Category catOriginal = Category.builder()
                .id(1L)
                .nombre("Original")
                .build();

        Category catNueva = Category.builder()
                .id(2L)
                .nombre("Nueva")
                .build();

        Product existing = Product.builder()
                .id(id)
                .nombre("Viejo")
                .descripcion("Desc vieja")
                .precioNormal(BigDecimal.valueOf(1000))
                .precioOferta(null)
                .categoria(catOriginal)
                .imagenUrl("old.jpg")
                .activo(true)
                .build();

        ProductRequest request = new ProductRequest();
        request.setNombre("Actualizado");
        request.setDescripcion("Desc nueva");
        request.setPrecioNormal(BigDecimal.valueOf(2000));
        request.setPrecioOferta(BigDecimal.valueOf(1500));
        request.setImagenUrl("new.jpg");
        request.setActivo(false);
        request.setCategoriaId(2L);

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryService.getEntity(2L)).thenReturn(catNueva);
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductDto result = productService.update(id, request);

        assertEquals("Actualizado", result.getNombre());
        assertEquals("Desc nueva", result.getDescripcion());
        assertEquals(BigDecimal.valueOf(2000), result.getPrecioNormal());
        assertEquals(BigDecimal.valueOf(1500), result.getPrecioOferta());
        assertEquals("new.jpg", result.getImagenUrl());
        assertFalse(result.isActivo());
        assertEquals(2L, result.getCategoriaId());

        verify(productRepository).findById(id);
        verify(categoryService).getEntity(2L);
        verify(productRepository).save(existing);
    }

    @Test
    void delete_conIdInexistente_lanzaExcepcion() {
        Long id = 9L;
        when(productRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> productService.delete(id));
        verify(productRepository).existsById(id);
        verify(productRepository, never()).deleteById(anyLong());
    }
}
