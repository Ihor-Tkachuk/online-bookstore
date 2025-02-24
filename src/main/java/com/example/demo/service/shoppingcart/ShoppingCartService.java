package com.example.demo.service.shoppingcart;

import com.example.demo.dto.cartitem.CreateCartItemRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto addCartItem(Long userId, CreateCartItemRequestDto requestDto);

    void createUsersCart(Long userId);

    ShoppingCartResponseDto getUsersCart(Long userId);

    ShoppingCartResponseDto updateCartItemQuantity(Long cartItemId,
                                                   UpdateCartItemRequestDto requestDto);

    void removeCartItem(Long cartItemId);
}
