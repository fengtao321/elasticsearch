package com.demo.elasticsearch.service;


import com.demo.elasticsearch.model.Book;
import com.demo.elasticsearch.service.exception.BookNotFoundException;
import com.demo.elasticsearch.service.exception.DuplicateIsbnException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<Book> getByIsbn(String isbn);

    List<Book> getAll();

    List<Book> findByAuthor(String authorName);

    List<Book> findByTitleAndAuthor(String title, String author);

    Book create(Book book) throws DuplicateIsbnException;

    void deleteById(String id);

    Book update(String id, Book book) throws BookNotFoundException;
}