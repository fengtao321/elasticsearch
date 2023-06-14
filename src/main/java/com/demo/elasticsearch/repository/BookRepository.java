package com.demo.elasticsearch.repository;

import com.demo.elasticsearch.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends ElasticsearchRepository<Book, String> {

    List<Book> findByAuthorName(String authorName);

    Optional<Book> findByIsbn(String isbn);
}