package microservices.common.events;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;
import java.util.UUID;

@Getter
@Setter
public abstract class BaseEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant createdAt = Instant.now();
}