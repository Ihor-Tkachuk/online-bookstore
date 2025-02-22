package com.example.demo.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCartItemRequestDto {

    @NotNull(message = "Book ID cannot be null")
    private Long bookId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
