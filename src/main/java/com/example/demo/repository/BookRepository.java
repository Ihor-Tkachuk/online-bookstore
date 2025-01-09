package com.example.demo.repository;

import com.example.demo.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    Book getBookById(long id);

    List<Book> getAll();
}
