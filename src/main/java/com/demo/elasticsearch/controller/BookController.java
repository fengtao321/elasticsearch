package com.demo.elasticsearch.controller;

import com.demo.elasticsearch.model.Document;
import com.demo.elasticsearch.service.DocumentService;
import com.demo.elasticsearch.service.exception.DocumentNotFoundException;
import com.demo.elasticsearch.service.exception.DuplicateDocumentException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Document createDocument(@Valid @RequestBody Document document) throws DuplicateDocumentException {
        return documentService.create(document);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{title}")
    public Document getBookByTitle(@PathVariable String title) throws DocumentNotFoundException {
        return documentService.getByTitle(title).orElseThrow(() -> new DocumentNotFoundException("The given document is not exist"));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/query")
    public List<Document> getBooksByAuthorAndTitle(@RequestParam(value = "title") String title, @RequestParam(value = "author") String author) {
        return documentService.findByTitleAndAuthor(title, author);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}")
    public Document updateDocument(@PathVariable String id, @RequestBody Document document) throws DocumentNotFoundException {
        return documentService.update(id, document);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void deleteDocument(@PathVariable String id) {
        documentService.deleteById(id);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping(value = "/search")
    public List<Document> searchDocument(@RequestParam(value = "searchText") String searchText) {
        return documentService.search(searchText);
    }

}