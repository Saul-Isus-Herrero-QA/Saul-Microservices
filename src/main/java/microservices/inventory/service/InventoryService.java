package microservices.inventory.service;

import microservices.inventory.model.ProductInventory;
import microservices.inventory.repository.ProductInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final ProductInventoryRepository inventoryRepository;

    @Transactional
    public void deductStock(List<String> productIds) {
        for (String productId : productIds) {
            // Leemos aplicando el bloqueo pesimista en PostgreSQL
            ProductInventory inventory = inventoryRepository.findByIdForUpdate(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado en inventario: " + productId));

            if (inventory.getStock() < 1) {
                throw new IllegalStateException("Stock insuficiente para el producto: " + productId);
            }

            inventory.setStock(inventory.getStock() - 1);
            inventoryRepository.save(inventory);
            log.info("Stock actualizado para el producto: {}. Nuevo stock: {}", productId, inventory.getStock());
        }
    }
}
