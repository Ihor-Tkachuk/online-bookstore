package com.example.demo.repository;

import com.example.demo.exception.DataProcessingException;
import com.example.demo.model.Book;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert a book: " + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Book> findBookById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Book book = session.get(Book.class, id);
            return Optional.ofNullable(book);
        } catch (Exception e) {
            throw new DataProcessingException("Can't get book by id " + id, e);
        }
    }

    @Override
    public List<Book> getAll() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Book";
            Query<Book> query = session.createQuery(hql, Book.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get all books", e);
        }
    }
}

