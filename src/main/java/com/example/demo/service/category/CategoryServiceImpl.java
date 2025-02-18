package com.example.demo.service.category;

import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.dto.category.CreateCategoryRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.category.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryResponseDto save(CreateCategoryRequestDto requestDto) {
        Category category = categoryMapper.toEntity(requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryResponseDto update(Long id, CreateCategoryRequestDto requestDto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id " + id));
        categoryMapper.updateCategory(existingCategory, requestDto);
        categoryRepository.save(existingCategory);
        return categoryMapper.toDto(existingCategory);
    }

    @Override
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id " + id));
        categoryRepository.delete(category);
    }
}
