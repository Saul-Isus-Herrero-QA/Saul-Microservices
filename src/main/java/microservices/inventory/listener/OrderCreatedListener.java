package microservices.inventory.listener;

import microservices.common.events.OrderCreatedEvent;
import microservices.inventory.service.InventoryService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedListener {

    private final InventoryService inventoryService;

    @SqsListener("order-created-queue")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Procesando evento de inventario para la orden: {}", event.getOrderId());
        try {
            inventoryService.deductStock(event.getProductIds());
        } catch (Exception e) {
            log.error("Error al procesar el inventario para la orden {}: {}", event.getOrderId(), e.getMessage());
            // AQUI: se lanzaría un evento de compensación (Saga Pattern) si falla.
            throw e; 
        }
    }
}