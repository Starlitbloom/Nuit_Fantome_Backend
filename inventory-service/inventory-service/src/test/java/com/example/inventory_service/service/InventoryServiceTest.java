package com.example.inventory_service.service;

import com.example.inventory_service.model.InventoryItem;
import com.example.inventory_service.repository.InventoryItemRepository;
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
class InventoryServiceTest {

    @Mock
    private InventoryItemRepository inventoryItemRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void getAll_devuelveListaDesdeRepositorio() {
        InventoryItem i1 = InventoryItem.builder().id(1L).productId("org01").quantity(10).build();
        InventoryItem i2 = InventoryItem.builder().id(2L).productId("washi01").quantity(5).build();

        when(inventoryItemRepository.findAll()).thenReturn(List.of(i1, i2));

        List<InventoryItem> result = inventoryService.getAll();

        assertEquals(2, result.size());
        verify(inventoryItemRepository, times(1)).findAll();
    }

    @Test
    void getByProductId_existente_ok() {
        InventoryItem item = InventoryItem.builder().id(1L).productId("org01").quantity(10).build();
        when(inventoryItemRepository.findByProductId("org01")).thenReturn(Optional.of(item));

        InventoryItem result = inventoryService.getByProductId("org01");

        assertEquals("org01", result.getProductId());
        verify(inventoryItemRepository, times(1)).findByProductId("org01");
    }

    @Test
    void adjustStock_conSuficienteStock_ok() {
        InventoryItem item = InventoryItem.builder().id(1L).productId("org01").quantity(10).build();
        when(inventoryItemRepository.findByProductId("org01")).thenReturn(Optional.of(item));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenAnswer(inv -> inv.getArgument(0));

        InventoryItem result = inventoryService.adjustStock("org01", -3);

        assertEquals(7, result.getQuantity());
        verify(inventoryItemRepository, times(1)).save(any(InventoryItem.class));
    }

    @Test
    void adjustStock_conStockInsuficiente_lanzaExcepcion() {
        InventoryItem item = InventoryItem.builder().id(1L).productId("org01").quantity(2).build();
        when(inventoryItemRepository.findByProductId("org01")).thenReturn(Optional.of(item));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> inventoryService.adjustStock("org01", -5));

        assertTrue(ex.getMessage().contains("Stock insuficiente"));
    }
}
