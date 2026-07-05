package microservices.shipping.service;

import microservices.shipping.model.Shipment;
import microservices.shipping.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingService {

    private final ShipmentRepository shipmentRepository;

    @Transactional
    public void createShipment(String orderId, String customerId) {
        // Se verifica si la orden ya tiene un envío asignado
        boolean shipmentExists = shipmentRepository.findAll().stream()
                .anyMatch(s -> s.getOrderId().equals(orderId));

        if (shipmentExists) {
            log.warn("El envío para la orden {} ya fue procesado anteriormente. Omitiendo duplicado.", orderId);
            return;
        }

        // Si no existe, procedemos a crear el registro de envío
        Shipment shipment = new Shipment();
        shipment.setOrderId(orderId);
        shipment.setCustomerId(customerId);
        shipment.setShippingStatus("PREPARANDO"); // Estado inicial del envío.

        shipmentRepository.save(shipment);
        log.info("Envío creado exitosamente para la orden: {}", orderId);
    }
}