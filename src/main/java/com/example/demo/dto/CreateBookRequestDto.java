package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NonNull;

@Data
public class CreateBookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    @Size(min = 13, max = 13)
    private String isbn;
    @NonNull
    @Min(0)
    private BigDecimal price;
    @NotBlank
    @Size(max = 20)
    private String description;
    @NotBlank
    private String coverImage;
}
