package com.example.demo.controller;

import com.example.demo.dto.order.CreateOrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.UpdateOrderStatusRequestDto;
import com.example.demo.dto.orderitem.OrderItemResponseDto;
import com.example.demo.model.User;
import com.example.demo.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders management",
        description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place an order",
            description = "Place an order")
    public OrderResponseDto createOrder(Authentication authentication,
                                        @RequestBody @Valid CreateOrderRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();

        return orderService.addOrder(user, requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Retrieve user's order history",
            description = "Retrieve user's order history")
    public List<OrderResponseDto> getOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return orderService.getAll(user);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Retrieve all OrderItems for a specific order",
            description = "Retrieve all OrderItems for a specific order")
    public Page<OrderItemResponseDto> getOrderItems(Authentication authentication,
                                                    @PathVariable Long orderId,
                                                    Pageable pageable) {
        User user = (User) authentication.getPrincipal();

        return orderService.getOrderItemsByOrderId(user, orderId, pageable);
    }

    @GetMapping("/{orderId}/items/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Retrieve a specific order item",
            description = "Retrieve  a specific order item within an order")
    public OrderItemResponseDto getOrderItem(Authentication authentication,
                                             @PathVariable Long orderId,
                                             @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();

        return orderService.getOrderItemById(user, orderId, id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status",
            description = "Update the status of an order")
    public OrderResponseDto updateOrderStatus(Authentication authentication,
                                              @PathVariable Long id,
                                              @RequestBody
                                                  @Valid UpdateOrderStatusRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();

        return orderService.updateOrderStatus(user, id, requestDto);
    }
}
