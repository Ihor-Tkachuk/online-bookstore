package com.example.demo.repository.orderitem;

import com.example.demo.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrderId(Long id);

    Optional<OrderItem> findByIdAndOrderId(Long id, Long orderId);
}
