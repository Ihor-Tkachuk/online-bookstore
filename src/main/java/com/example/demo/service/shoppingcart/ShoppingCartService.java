package com.example.demo.service.shoppingcart;

import com.example.demo.dto.cartitem.CartItemResponseDto;
import com.example.demo.dto.cartitem.CreateCartItemRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.model.ShoppingCart;

public interface ShoppingCartService {
    CartItemResponseDto addCartItem(ShoppingCart shoppingCart, CreateCartItemRequestDto requestDto);

    ShoppingCart getOrCreateShoppingCart(Long userId);

    ShoppingCartResponseDto getShoppingCart(Long userId);

    CartItemResponseDto updateCartItemQuantity(Long cartItemId,
                                               UpdateCartItemRequestDto requestDto);

    void removeCartItem(Long cartItemId);
}
