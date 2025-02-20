package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.dto.category.CreateCategoryRequestDto;
import com.example.demo.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto categoryDto);

    Category updateCategory(@MappingTarget Category category, CreateCategoryRequestDto requestDto);
}
