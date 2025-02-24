package com.example.demo.controller;

import com.example.demo.dto.cartitem.CreateCartItemRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.model.User;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping сart management",
        description = "Endpoints for managing shopping сart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add book to the shopping cart",
            description = "Add book to the shopping cart")
    public ShoppingCartResponseDto addCartItemToCart(
            Authentication authentication,
            @RequestBody @Valid CreateCartItemRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();

        return shoppingCartService.addCartItem(user.getId(), requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get shopping cart",
            description = "Get user's shopping cart")
    public ShoppingCartResponseDto getShoppingCart(
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return shoppingCartService.getUsersCart(user.getId());
    }

    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update quantity of a book",
            description = "Update quantity of a book in the shopping cart")
    public ShoppingCartResponseDto updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequestDto requestDto) {

        return shoppingCartService.updateCartItemQuantity(cartItemId, requestDto);
    }

    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a book from the shopping cart",
            description = "Remove a book from the shopping cart by its ID")
    public ShoppingCartResponseDto removeCartItem(
            Authentication authentication,
            @PathVariable Long cartItemId) {
        shoppingCartService.removeCartItem(cartItemId);

        User user = (User) authentication.getPrincipal();

        return shoppingCartService.getUsersCart(user.getId());
    }
}
