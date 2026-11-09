package re.edu.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final JdbcTemplate jdbcTemplate;
    private final RedissonClient redissonClient;

    public boolean deductStock(Long productId, Integer quantity) {

        String lockKey = "lock:product:" + productId;

        RLock lock = redissonClient.getLock(lockKey);

        boolean locked = false;

        try {
            // Lấy khóa phân tán
            lock.lock();
            locked = true;

            // Kiểm tra và trừ tồn kho
            String sql = """
                    UPDATE products
                    SET stock = stock - ?
                    WHERE id = ? AND stock >= ?
                    """;

            int updated = jdbcTemplate.update(
                    sql,
                    quantity,
                    productId,
                    quantity
            );

            return updated > 0;

        } finally {
            // Giải phóng khóa
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}