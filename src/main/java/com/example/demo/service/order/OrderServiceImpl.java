package com.example.demo.service.order;

import com.example.demo.dto.order.CreateOrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.UpdateOrderStatusRequestDto;
import com.example.demo.dto.orderitem.OrderItemResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.OrderProcessingException;
import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.Book;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.order.OrderRepository;
import com.example.demo.repository.orderitem.OrderItemRepository;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderResponseDto addOrder(User user, CreateOrderRequestDto requestDto) {
        ShoppingCart shoppingCart = getUsersShoppingCart(user);

        Order order = createUsersOrder(user, requestDto);

        getOrderItemsAndSetToOrder(shoppingCart, order);

        orderRepository.save(order);

        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);

        return orderMapper.toOrderResponseDto(order);
    }

    private void getOrderItemsAndSetToOrder(ShoppingCart shoppingCart, Order order) {
        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    Book book = bookRepository.findById(cartItem.getBook().getId())
                            .orElseThrow(() -> new EntityNotFoundException("Book not found by ID: "
                                    + cartItem.getBook().getId()));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setBook(book);
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(book.getPrice());
                    return orderItem;
                }).collect(Collectors.toSet());
        orderItemRepository.saveAll(orderItems);
        order.setOrderItems(orderItems);

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) {
            total = total.add(orderItem.getPrice());
        }
        order.setTotal(total);
    }

    private Order createUsersOrder(User user, CreateOrderRequestDto requestDto) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.getShippingAddress());
        order.setTotal(BigDecimal.ZERO);
        orderRepository.save(order);
        return order;
    }

    private ShoppingCart getUsersShoppingCart(User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new OrderProcessingException(
                    "The order cannot be formed because the shopping cart with ID "
                            + shoppingCart.getId() + " is empty");
        }
        return shoppingCart;
    }

    @Override
    public Page<OrderResponseDto> getAll(User user, Pageable pageable) {
        return orderRepository.findAllByUserId(user.getId(), pageable)
                .map(orderMapper::toOrderResponseDto);
    }

    @Override
    public List<OrderItemResponseDto> getOrderItemsByOrderId(
            User user, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found by ID: " + orderId
                                + " for user with ID: " + user.getId()));

        return orderItemRepository.findAllByOrderId(orderId).stream()
                .map(orderItemMapper::toOrderItemResponseDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getOrderItemById(User user, Long orderId, Long id) {
        OrderItem orderItem = orderItemRepository
                .findByIdAndOrderIdAndUserId(id, orderId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order item not found with ID: " + id
                                + " for order with ID: " + orderId
                                + " of user with ID: " + user.getId()));

        return orderItemMapper.toOrderItemResponseDto(orderItem);
    }

    @Override
    public OrderResponseDto updateOrderStatus(
            User user, Long id, UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found by ID: " + id));
        order.setStatus(requestDto.getStatus());

        return orderMapper.toOrderResponseDto(orderRepository.save(order));
    }
}
