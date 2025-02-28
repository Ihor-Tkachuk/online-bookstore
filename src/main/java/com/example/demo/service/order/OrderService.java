package com.example.demo.service.order;

import com.example.demo.dto.order.CreateOrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.UpdateOrderStatusRequestDto;
import com.example.demo.dto.orderitem.OrderItemResponseDto;
import com.example.demo.model.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto addOrder(User user, CreateOrderRequestDto requestDto);

    List<OrderResponseDto> getAll(User user);

    Page<OrderItemResponseDto> getOrderItemsByOrderId(User user, Long orderId, Pageable pageable);

    OrderItemResponseDto getOrderItemById(User user, Long orderId, Long id);

    OrderResponseDto updateOrderStatus(User user, Long id, UpdateOrderStatusRequestDto requestDto);
}
