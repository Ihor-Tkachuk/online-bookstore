package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NonNull;

@Data
public class CreateBookRequestDto {
    @NonNull
    private String title;
    @NonNull
    private String author;
    @NonNull
    @Size(min = 13, max = 13, message = "The ISBN code must be exactly 13 characters")
    private String isbn;
    @NonNull
    @Min(0)
    private BigDecimal price;
    @NonNull
    @Size(max = 20, message = "The description should not exceed 20 characters.")
    private String description;
    @NonNull
    private String coverImage;
}
