package microservices.order.service;

import microservices.common.events.OrderCreatedEvent;
import microservices.common.publisher.EventPublisher;
import microservices.order.model.Order;
import microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final EventPublisher eventPublisher; // Se inyecta la interfaz de common.

    @Transactional
    public Order createOrder(Order order) {
        order.setStatus("PENDING");
        Order savedOrder = orderRepository.save(order);

        // Se mapeamos al evento para notificar a AWS Simple Queue Service (SQS).
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(savedOrder.getId());
        event.setCustomerId(savedOrder.getCustomerId());
        event.setTotalAmount(savedOrder.getTotalAmount());
        event.setProductIds(savedOrder.getProductIds());

        // Se publica el evento al destino (Nombre de la cola SQS.
        eventPublisher.publish("order-created-queue", event);

        return savedOrder;
    }
        
}
