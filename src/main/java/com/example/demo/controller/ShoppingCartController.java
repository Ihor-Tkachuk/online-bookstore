package com.example.demo.controller;

import com.example.demo.dto.cartitem.CartItemResponseDto;
import com.example.demo.dto.cartitem.CreateCartItemRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.model.ShoppingCart;
import com.example.demo.security.AuthenticationService;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping сart management",
        description = "Endpoints for managing shopping сart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final AuthenticationService authenticationService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add book to the shopping cart",
            description = "Add book to the shopping cart")
    public ResponseEntity<CartItemResponseDto> addCartItemToCart(
            @RequestBody @Valid CreateCartItemRequestDto requestDto) {

        Long userId = authenticationService.getCurrentUserId();
        ShoppingCart shoppingCart = shoppingCartService.getOrCreateShoppingCart(userId);
        CartItemResponseDto cartItemResponseDto = shoppingCartService
                .addCartItem(shoppingCart, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemResponseDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get shopping cart",
            description = "Get user's shopping cart")
    public ResponseEntity<ShoppingCartResponseDto> getShoppingCart() {
        Long userId = authenticationService.getCurrentUserId();
        ShoppingCartResponseDto shoppingCartResponseDto = shoppingCartService
                .getShoppingCart(userId);

        return ResponseEntity.ok(shoppingCartResponseDto);
    }

    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update quantity of a book",
            description = "Update quantity of a book in the shopping cart")
    public ResponseEntity<CartItemResponseDto> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequestDto requestDto) {

        CartItemResponseDto updatedCartItemDto = shoppingCartService
                .updateCartItemQuantity(cartItemId, requestDto);

        return ResponseEntity.ok(updatedCartItemDto);
    }

    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Remove a book from the shopping cart",
            description = "Remove a book from the shopping cart by its ID")
    public ResponseEntity<ShoppingCartResponseDto> removeCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.removeCartItem(cartItemId);

        Long userId = authenticationService.getCurrentUserId();
        ShoppingCartResponseDto shoppingCartResponseDto = shoppingCartService
                .getShoppingCart(userId);

        return ResponseEntity.ok(shoppingCartResponseDto);
    }
}
