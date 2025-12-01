package com.example.inventory_service.controller;

import com.example.inventory_service.model.InventoryItem;
import com.example.inventory_service.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    @Test
    void getAll_devuelveLista() {
        InventoryItem i1 = InventoryItem.builder().id(1L).productId("org01").quantity(10).build();
        when(inventoryService.getAll()).thenReturn(List.of(i1));

        List<InventoryItem> result = inventoryController.getAll();

        assertEquals(1, result.size());
        verify(inventoryService, times(1)).getAll();
    }

    @Test
    void getByProductId_existente_devuelve200() {
        InventoryItem item = InventoryItem.builder().id(1L).productId("org01").quantity(10).build();
        when(inventoryService.getByProductId("org01")).thenReturn(item);

        ResponseEntity<InventoryItem> response = inventoryController.getByProductId("org01");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("org01", response.getBody().getProductId());
        verify(inventoryService, times(1)).getByProductId("org01");
    }
}
