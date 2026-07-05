package microservices.common.publisher;

import microservices.common.events.BaseEvent;

public interface EventPublisher {
    <T extends BaseEvent> void publish(String destination, T event);
}