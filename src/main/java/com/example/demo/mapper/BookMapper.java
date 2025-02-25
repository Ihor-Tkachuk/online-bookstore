package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import com.example.demo.repository.book.BookRepository;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = {BookRepository.class})
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    Book updateBook(@MappingTarget Book book, CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        List<Long> categoryIds = book.getCategories().stream()
                .map(Category::getId)
                .toList();
        bookDto.setCategoryIds(categoryIds);
    }

    @Named("bookFromId")
    default Book bookFromId(Long id, @Context BookRepository bookRepository) {
        return bookRepository.findByIdWithCategories(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
    }
}
