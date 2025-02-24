package com.example.demo.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateCartItemRequestDto {

    @NotNull(message = "Book ID cannot be null")
    @Positive
    private Long bookId;

    @Positive
    private int quantity;
}
