package microservices.order.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private String customerId;
    private List<String> productIds;
}