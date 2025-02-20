package com.example.demo.service.book;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.BookSearchParameters;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.book.BookSpecificationBuilder;
import com.example.demo.repository.category.CategoryRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);

        Set<Category> categories = mapCategoryIdsToCategories(
                requestDto.getCategoryIds());
        book.setCategories(categories);

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't get book by id " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't get book by id " + id));
        bookMapper.updateBook(book, requestDto);

        Set<Category> categories = mapCategoryIdsToCategories(
                requestDto.getCategoryIds());
        book.setCategories(categories);

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> search(BookSearchParameters params, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification, pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public Page<BookDtoWithoutCategoryIds> getAllByCategoryId(Long categoryId, Pageable pageable) {
        return bookRepository.findAllByCategoryId(categoryId, pageable)
                .map(bookMapper::toDtoWithoutCategories);
    }

    public Set<Category> mapCategoryIdsToCategories(List<Long> categoryIds) {
        return categoryIds.stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new EntityNotFoundException("Category not found by id: "
                                + categoryId)))
                .collect(Collectors.toSet());
    }
}
