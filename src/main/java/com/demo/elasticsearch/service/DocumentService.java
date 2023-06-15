package com.demo.elasticsearch.service;


import com.demo.elasticsearch.dto.DocumentDto;
import com.demo.elasticsearch.model.Document;
import com.demo.elasticsearch.service.exception.DocumentNotFoundException;
import com.demo.elasticsearch.service.exception.DuplicateDocumentException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface DocumentService {
    Document create(Document document) throws DuplicateDocumentException;

    Document update(String id, DocumentDto documentDto) throws DocumentNotFoundException;

    Optional<Document> getByTitle(String title);

    Optional<Document> getByTitleAndAuthor(String title, String author);

    List<Document> getAll();

    void deleteById(String id);

    List<Document> searchBlocking(String searchText);

    CompletableFuture<List<Document>> search(String searchText);

    Document convertToEntity(DocumentDto documentDtoDto);
}