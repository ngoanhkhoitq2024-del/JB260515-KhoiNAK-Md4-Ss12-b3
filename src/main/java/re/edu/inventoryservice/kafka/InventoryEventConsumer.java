package re.edu.inventoryservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import re.edu.inventoryservice.service.InventoryService;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "order-events",
            groupId = "inventory-group"
    )
    public void consume(OrderCreatedEvent event) {

        log.info("Received OrderCreatedEvent: {}", event.getOrderId());

        boolean success = inventoryService.deductStock(
                event.getProductId(),
                event.getQuantity()
        );

        if (success) {
            log.info("Inventory deducted successfully!");
            log.info("Product ID: {}", event.getProductId());
            log.info("Quantity: {}", event.getQuantity());
        } else {
            log.warn("Sản phẩm đã hết hàng!");
            log.warn("Product ID: {}", event.getProductId());
        }
    }
}