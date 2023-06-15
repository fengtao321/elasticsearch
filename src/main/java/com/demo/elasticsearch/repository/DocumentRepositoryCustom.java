package com.demo.elasticsearch.repository;

import com.demo.elasticsearch.model.Document;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepositoryCustom {
    public Optional<Document> findByTitleAuthor(String title, String author);
}
