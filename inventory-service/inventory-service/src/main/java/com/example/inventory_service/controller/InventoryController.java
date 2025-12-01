package com.example.inventory_service.controller;

import com.example.inventory_service.model.InventoryItem;
import com.example.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public List<InventoryItem> getAll() {
        return inventoryService.getAll();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryItem> getByProductId(@PathVariable String productId) {
        try {
            return ResponseEntity.ok(inventoryService.getByProductId(productId));
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<InventoryItem> save(@RequestBody InventoryItem item) {
        InventoryItem saved = inventoryService.save(item);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/{productId}/adjust")
    public ResponseEntity<InventoryItem> adjustStock(
            @PathVariable String productId,
            @RequestParam int delta
    ) {
        try {
            InventoryItem updated = inventoryService.adjustStock(productId, delta);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            if (ex.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }
}
