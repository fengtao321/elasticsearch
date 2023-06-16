package com.demo.elasticsearch.repository;

import com.demo.elasticsearch.model.Document;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
public interface DocumentRepositoryCustom {
    public Optional<Document> findByTitleAuthor(String title, String author);
    public List<Document> search(String searchText) throws IOException;
    public CompletableFuture<List<Document>> searchAsync(String searchText);
}
