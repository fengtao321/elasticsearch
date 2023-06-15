package com.demo.elasticsearch.service;


import com.demo.elasticsearch.dto.DocumentDto;
import com.demo.elasticsearch.model.Document;
import com.demo.elasticsearch.service.exception.DocumentNotFoundException;
import com.demo.elasticsearch.service.exception.DuplicateDocumentException;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface DocumentService {

    Optional<Document> getByTitle(String title);

    List<Document> getAll();

    List<Document> findByAuthor(String author);

    List<Document> findByTitleAndAuthor(String title, String author);

    Document create(Document document) throws DuplicateDocumentException;

    void deleteById(String id);

    Document update(String id, DocumentDto documentDto) throws DocumentNotFoundException, ParseException;

    List<Document> search(String searchText);

    CompletableFuture<List<Document>> searchAsync(String searchText) throws InterruptedException;
}