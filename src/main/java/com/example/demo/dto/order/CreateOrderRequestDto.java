package com.example.demo.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    @NotBlank(message = "Shipping address cannot be blank")
    private String shippingAddress;
}
