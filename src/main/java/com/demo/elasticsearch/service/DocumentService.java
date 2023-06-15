package com.demo.elasticsearch.service;


import com.demo.elasticsearch.model.Document;
import com.demo.elasticsearch.service.exception.DocumentNotFoundException;
import com.demo.elasticsearch.service.exception.DuplicateDocumentException;

import java.util.List;
import java.util.Optional;

public interface DocumentService {

    Optional<Document> getByTitle(String title);

    List<Document> getAll();

    List<Document> findByAuthor(String author);

    List<Document> findByTitleAndAuthor(String title, String author);

    Document create(Document document) throws DuplicateDocumentException;

    void deleteById(String id);

    Document update(String id, Document document) throws DocumentNotFoundException;

    List<Document> search(String searchText);
}