package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookSearchParameters {
    @NotNull
    @Size(min = 1, max = 1)
    private String[] titles;

    @NotNull
    private String[] authors;

    @NotNull
    private String[] isbns;
}
