package com.example.demo.service.book;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.BookSearchParameters;
import com.example.demo.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> getAll(Pageable pageable);

    BookDto getBookById(Long id);

    void deleteById(Long id);

    BookDto updateBook(Long id, CreateBookRequestDto requestDto);

    Page<BookDto> search(BookSearchParameters params, Pageable pageable);

    Page<BookDtoWithoutCategoryIds> getAllByCategoryId(Long id, Pageable pageable);
}
