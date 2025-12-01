package com.example.inventory_service.service;

import com.example.inventory_service.model.InventoryItem;
import com.example.inventory_service.repository.InventoryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryItemRepository inventoryItemRepository;

    public List<InventoryItem> getAll() {
        return inventoryItemRepository.findAll();
    }

    public InventoryItem getByProductId(String productId) {
        return inventoryItemRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en inventario"));
    }

    public InventoryItem save(InventoryItem item) {
        // si existe ese productId, actualizamos cantidad
        return inventoryItemRepository.findByProductId(item.getProductId())
                .map(existing -> {
                    existing.setQuantity(item.getQuantity());
                    return inventoryItemRepository.save(existing);
                })
                .orElseGet(() -> inventoryItemRepository.save(item));
    }

    public InventoryItem adjustStock(String productId, int delta) {
        InventoryItem item = inventoryItemRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en inventario"));

        int nuevaCantidad = item.getQuantity() + delta;
        if (nuevaCantidad < 0) {
            throw new RuntimeException("Stock insuficiente para producto " + productId);
        }
        item.setQuantity(nuevaCantidad);
        return inventoryItemRepository.save(item);
    }
}
