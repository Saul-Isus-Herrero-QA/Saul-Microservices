package microservices.order.controller;

import microservices.order.dto.OrderRequest;
import microservices.order.model.Order;
import microservices.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        // Mapeo del DTO a la Entidad
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setProductIds(request.getProductIds());
        
        // Se hace la consulta al servicio de productos 
        // para calcular el total real. Al ser un proyecto personal de Saul, asignamos un valor fijo.
        order.setTotalAmount(BigDecimal.valueOf(99.99));

        Order createdOrder = orderService.createOrder(order);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }
}