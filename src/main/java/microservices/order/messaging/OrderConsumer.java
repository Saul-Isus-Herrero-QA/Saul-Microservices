package microservices.order.messaging;

import microservices.common.idempotency.IdempotentConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    @KafkaListener(topics = "order-events-topic", groupId = "order-processing-group")
    @IdempotentConsumer(consumerGroup = "order-processing-group")
    public void handleOrderCreated(
            @Payload String orderJson,
            @Header(KafkaHeaders.RECEIVED_KEY) String orderId) {
        
        // El aspecto ya garantizó que este orderId no se ha procesado antes
        System.out.println("Procesando evento único: " + orderId);
    }
}