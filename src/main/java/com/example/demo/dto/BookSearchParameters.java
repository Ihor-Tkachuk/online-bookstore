package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookSearchParameters {
    @NotEmpty
    @Size(min = 1, max = 1)
    private String[] titles;

    @NotEmpty
    private String[] authors;

    @NotEmpty
    private String[] isbns;
}
