package microservices.common.events;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderCreatedEvent extends BaseEvent {
    private String orderId;
    private String customerId;
    private BigDecimal totalAmount;
    private List<String> productIds; 
}
