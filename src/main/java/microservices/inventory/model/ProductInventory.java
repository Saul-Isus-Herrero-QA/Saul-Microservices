package microservices.inventory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_inventory")
@Getter
@Setter
@NoArgsConstructor
public class ProductInventory {
    @Id
    private String productId; // Usamos el mismo ID de producto que viene en los eventos

    @Column(nullable = false)
    private Integer stock;
}