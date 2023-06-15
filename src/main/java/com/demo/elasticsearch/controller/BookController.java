package com.demo.elasticsearch.controller;

import com.demo.elasticsearch.dto.DocumentDto;
import com.demo.elasticsearch.model.Document;
import com.demo.elasticsearch.service.DocumentService;
import com.demo.elasticsearch.service.exception.DocumentNotFoundException;
import com.demo.elasticsearch.service.exception.DuplicateDocumentException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/doc")
public class BookController {
    private final DocumentService documentService;
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Document> getAllDocuments() {
        return documentService.getAll();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public Document createDocument(@Valid @RequestBody DocumentDto documentDto) throws DuplicateDocumentException {
        return documentService.create(documentService.convertToEntity(documentDto));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{title}/{author}")
    public Document getBookByTitle(@PathVariable String title, @RequestParam(value = "author",required = false) String author) throws DocumentNotFoundException {
        Optional<Document> document;
        if(author.isBlank()) {
            document =documentService.getByTitle(title);
        } else {
            document = documentService.getByTitleAndAuthor(title, author);
        }
        return document.orElseThrow(() -> new DocumentNotFoundException("The given document is not exist"));
    }


    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}")
    public Document updateDocument(@PathVariable String id, @RequestBody DocumentDto documentDto) throws DocumentNotFoundException {
        return documentService.update(id, documentDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void deleteDocument(@PathVariable String id) {
        documentService.deleteById(id);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping(value = "/searchBlocking")
    public List<Document> searchDocumentBlocking(@RequestParam(value = "searchText") String searchText) {
        return documentService.searchBlocking(searchText);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping(value = "/search")
    public CompletableFuture<List<Document>> searchDocument(@RequestParam(value = "searchText") String searchText) {
        return documentService.search(searchText);
    }

}