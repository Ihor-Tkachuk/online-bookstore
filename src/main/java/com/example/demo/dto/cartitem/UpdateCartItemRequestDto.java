package com.example.demo.dto.cartitem;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCartItemRequestDto {
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
