package com.demo.elasticsearch.repository;

import com.demo.elasticsearch.model.Document;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends ElasticsearchRepository<Document, String> {
    Optional<Document> findByTitle(String title);
}
