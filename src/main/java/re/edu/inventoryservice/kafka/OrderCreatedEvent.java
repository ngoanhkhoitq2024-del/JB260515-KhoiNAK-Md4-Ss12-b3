package re.edu.inventoryservice.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private String customerName;
    private String customerEmail;
    private Long productId;
    private Integer quantity;
    private Double totalAmount;
    private LocalDateTime createdAt;
}