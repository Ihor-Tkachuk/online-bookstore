package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NonNull;

@Data
public class CreateBookRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String author;
    @NotNull
    @Size(min = 13, max = 13)
    private String isbn;
    @NonNull
    @Min(0)
    private BigDecimal price;
    @NotNull
    @Size(max = 20)
    private String description;
    @NotNull
    private String coverImage;
}
