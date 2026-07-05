package microservices.shipping.listener;

import microservices.common.events.OrderCreatedEvent;
import microservices.shipping.service.ShippingService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedListener {

    private final ShippingService shippingService;

    @SqsListener("order-created-queue")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Recibido evento de orden creada en Shipping Service para el ID: {}", event.getOrderId());
        shippingService.createShipment(event.getOrderId(), event.getCustomerId());
    }
}