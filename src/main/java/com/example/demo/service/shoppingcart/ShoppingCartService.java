package com.example.demo.service.shoppingcart;

import com.example.demo.dto.cartitem.CreateCartItemRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto addCartItem(Long userId, CreateCartItemRequestDto requestDto);

    void createUsersCart(User user);

    ShoppingCartResponseDto getUsersCart(Long userId);

    ShoppingCartResponseDto updateCartItemQuantity(User user,
                                                   Long cartItemId,
                                                   UpdateCartItemRequestDto requestDto);

    void removeCartItem(Long cartItemId);
}
