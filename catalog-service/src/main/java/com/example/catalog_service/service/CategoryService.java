package com.example.catalog_service.service;

import com.example.catalog_service.dto.CategoryDto;
import com.example.catalog_service.dto.CategoryRequest;
import com.example.catalog_service.model.Category;
import com.example.catalog_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Públicos: solo categorías activas
    public List<CategoryDto> getActiveCategories() {
        return categoryRepository.findByActivaTrue()
                .stream()
                .map(this::toDto)
                .toList();
    }

    // Admin: crear
    public CategoryDto create(CategoryRequest request) {
        Category category = Category.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .activa(request.getActiva() == null ? true : request.getActiva())
                .build();

        return toDto(categoryRepository.save(category));
    }

    // Admin: actualizar
    public CategoryDto update(Long id, CategoryRequest request) {
        Category cat = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (request.getNombre() != null) cat.setNombre(request.getNombre());
        if (request.getDescripcion() != null) cat.setDescripcion(request.getDescripcion());
        if (request.getActiva() != null) cat.setActiva(request.getActiva());

        return toDto(categoryRepository.save(cat));
    }

    // Admin: eliminar
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoryRepository.deleteById(id);
    }

    // Uso interno: obtener entidad
    public Category getEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    private CategoryDto toDto(Category c) {
        return CategoryDto.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .descripcion(c.getDescripcion())
                .activa(c.isActiva())
                .build();
    }
}
