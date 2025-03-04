package com.example.demo.repository.orderitem;

import com.example.demo.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrderId(Long id);

    @Query(value = """
            SELECT oi FROM OrderItem oi
            JOIN FETCH oi.order o
            WHERE oi.id = :orderItemId AND o.id = :orderId AND o.user.id = :userId""")
    Optional<OrderItem> findByIdAndOrderIdAndUserId(@Param("orderItemId") Long orderItemId,
                                                    @Param("orderId") Long orderId,
                                                    @Param("userId") Long userId);
}
