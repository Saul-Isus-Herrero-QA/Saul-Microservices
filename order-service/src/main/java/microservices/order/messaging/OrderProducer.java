package microservices.order.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderProducer.class);
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "order-events-topic";

    public OrderProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Envía un evento de orden creada a Kafka de forma asíncrona.
     * Genera una clave única (UUID) para garantizar la idempotencia en el consumidor.
     */
    public void sendOrderCreatedEvent(String orderJson) {
        String messageId = UUID.randomUUID().toString();
        
        log.info("Preparando envío de evento de orden. Asignando Message ID / Key: {}", messageId);

        // Envío asíncronico utilizando CompletableFuture
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC, messageId, orderJson);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Evento enviado exitosamente a Kafka. Topic: {}, Partición: {}, Offset: {}", 
                        TOPIC, 
                        result.getRecordMetadata().partition(), 
                        result.getRecordMetadata().offset());
            } else {
                log.error("Error crítico al enviar el evento a Kafka para la orden {}: ", messageId, ex);
            }
        });
    }
}