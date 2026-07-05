package microservices.common.publisher;

import microservices.common.events.BaseEvent;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AwsSqsEventPublisher implements EventPublisher {

    private final SqsTemplate sqsTemplate;

    @Override
    public <T extends BaseEvent> void publish(String destination, T event) {
        // Envia el objeto evento directamente; SqsTemplate lo convierte a JSON de forma nativa.
        sqsTemplate.send(to -> to
                .queue(destination)
                .payload(event)
        );        
    }
    
}