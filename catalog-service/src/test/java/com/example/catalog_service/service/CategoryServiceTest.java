package com.example.catalog_service.service;

import com.example.catalog_service.dto.CategoryDto;
import com.example.catalog_service.dto.CategoryRequest;
import com.example.catalog_service.model.Category;
import com.example.catalog_service.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getActiveCategories_devuelveListaDeDtos() {
        // given
        Category cat = Category.builder()
                .id(1L)
                .nombre("Organizadores")
                .descripcion("Cositas para escritorio")
                .activa(true)
                .build();

        when(categoryRepository.findByActivaTrue()).thenReturn(List.of(cat));

        // when
        List<CategoryDto> result = categoryService.getActiveCategories();

        // then
        assertEquals(1, result.size());
        assertEquals("Organizadores", result.get(0).getNombre());
        verify(categoryRepository).findByActivaTrue();
    }

    @Test
    void create_guardaYDevuelveDto() {
        // given
        CategoryRequest request = new CategoryRequest();
        request.setNombre("Washi");
        request.setDescripcion("Cintas decorativas");
        request.setActiva(true);

        Category saved = Category.builder()
                .id(10L)
                .nombre("Washi")
                .descripcion("Cintas decorativas")
                .activa(true)
                .build();

        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        // when
        CategoryDto result = categoryService.create(request);

        // then
        assertNotNull(result.getId());
        assertEquals("Washi", result.getNombre());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void update_actualizaCamposCuandoExiste() {
        // given
        Long id = 5L;

        Category existing = Category.builder()
                .id(id)
                .nombre("Antiguo")
                .descripcion("Desc vieja")
                .activa(true)
                .build();

        CategoryRequest request = new CategoryRequest();
        request.setNombre("Nuevo nombre");
        request.setDescripcion("Nueva desc");
        request.setActiva(false);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        CategoryDto result = categoryService.update(id, request);

        // then
        assertEquals("Nuevo nombre", result.getNombre());
        assertEquals("Nueva desc", result.getDescripcion());
        assertFalse(result.isActiva());
        verify(categoryRepository).findById(id);
        verify(categoryRepository).save(existing);
    }

    @Test
    void delete_conIdExistente_elimina() {
        Long id = 3L;
        when(categoryRepository.existsById(id)).thenReturn(true);

        categoryService.delete(id);

        verify(categoryRepository).existsById(id);
        verify(categoryRepository).deleteById(id);
    }

    @Test
    void delete_conIdInexistente_lanzaExcepcion() {
        Long id = 99L;
        when(categoryRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> categoryService.delete(id));
        verify(categoryRepository).existsById(id);
        verify(categoryRepository, never()).deleteById(anyLong());
    }
}
