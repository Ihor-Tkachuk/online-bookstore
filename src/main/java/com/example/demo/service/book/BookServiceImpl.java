package com.example.demo.service.book;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookSearchParameters;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.exception.DataProcessingException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.book.BookSpecificationBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        try {
            Book book = bookMapper.toModel(requestDto);
            return bookMapper.toDto(bookRepository.save(book));
        } catch (Exception e) {
            throw new DataProcessingException("Error saving book", e);
        }
    }

    @Override
    public List<BookDto> getAll(Pageable pageable) {
        try {
            return bookRepository.findAll(pageable).stream()
                    .map(bookMapper::toDto)
                    .toList();
        } catch (Exception e) {
            throw new DataProcessingException("Error fetching all books", e);
        }
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
        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataProcessingException("Error deleting book by id", e);
        }
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto requestDto) {
        try {
            Book book = bookRepository.findById(id).orElseThrow(
                    () -> new EntityNotFoundException("Can't get book by id " + id));
            bookMapper.updateBook(book, requestDto);
            return bookMapper.toDto(bookRepository.save(book));
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataProcessingException("Error updating book", e);
        }
    }

    @Override
    public List<BookDto> search(BookSearchParameters params, Pageable pageable) {
        try {
            Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
            return bookRepository.findAll(bookSpecification, pageable).stream()
                    .map(bookMapper::toDto)
                    .toList();
        } catch (Exception e) {
            throw new DataProcessingException("Error searching books", e);
        }
    }
}
