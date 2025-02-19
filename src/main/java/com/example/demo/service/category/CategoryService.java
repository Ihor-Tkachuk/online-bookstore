package com.example.demo.service.category;

import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.dto.category.CreateCategoryRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CreateCategoryRequestDto requestDto);

    CategoryResponseDto update(Long id, CreateCategoryRequestDto requestDto);

    void deleteById(Long id);
}
