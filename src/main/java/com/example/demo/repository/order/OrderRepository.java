package com.example.demo.repository.order;

import com.example.demo.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long id);

    Optional<Order> findByIdAndUserId(Long orderId, Long id);
}
