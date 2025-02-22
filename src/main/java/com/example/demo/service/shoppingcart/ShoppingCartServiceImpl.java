package com.example.demo.service.shoppingcart;

import com.example.demo.dto.cartitem.CartItemResponseDto;
import com.example.demo.dto.cartitem.CreateCartItemRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.cartitem.CartItemRepository;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import com.example.demo.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final BookMapper bookMapper;

    @Override
    @Transactional
    public CartItemResponseDto addCartItem(ShoppingCart shoppingCart,
                                           CreateCartItemRequestDto requestDto) {
        Book book = bookMapper.bookFromId(requestDto.getBookId(), bookRepository);

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(requestDto.getQuantity());

        shoppingCart.addCartItem(cartItem);

        return cartItemMapper.toCartItemResponseDto(cartItemRepository.save(cartItem));
    }

    @Override
    @Transactional
    public ShoppingCart getOrCreateShoppingCart(Long userId) {
        return shoppingCartRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found by ID: "
                            + userId));
                    ShoppingCart shoppingCart = new ShoppingCart();
                    shoppingCart.setUser(user);
                    return shoppingCartRepository.save(shoppingCart);
                });
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto getShoppingCart(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user ID: " + userId));

        ShoppingCartResponseDto responseDto = shoppingCartMapper
                .toShoppingCartResponseDto(shoppingCart);
        responseDto.setCartItems(shoppingCart.getCartItems().stream()
                .map(cartItemMapper::toCartItemResponseDto)
                .toList());
        return responseDto;
    }

    @Override
    @Transactional
    public CartItemResponseDto updateCartItemQuantity(Long cartItemId,
                                                      UpdateCartItemRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found for ID: " + cartItemId));
        cartItem.setQuantity(requestDto.getQuantity());

        return cartItemMapper.toCartItemResponseDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found for ID: " + cartItemId));
        cartItemRepository.delete(cartItem);
    }
}
