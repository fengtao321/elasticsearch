package com.demo.elasticsearch.controller;

import com.demo.elasticsearch.dto.DocumentDto;
import com.demo.elasticsearch.model.Document;
import com.demo.elasticsearch.service.DocumentService;
import com.demo.elasticsearch.service.exception.DocumentNotFoundException;
import com.demo.elasticsearch.service.exception.DuplicateDocumentException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/doc")
public class BookController {
    private final DocumentService documentService;

    private final ModelMapper modelMapper;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Document> getAllDocuments() {
        return documentService.getAll();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public Document createDocument(@Valid @RequestBody DocumentDto documentDto) throws DuplicateDocumentException, ParseException {
        return documentService.create(convertToEntity(documentDto));
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
    public Document updateDocument(@PathVariable String id, @RequestBody DocumentDto documentDto) throws DocumentNotFoundException, ParseException {
        return documentService.update(id, documentDto);
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

    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping(value = "/searchAsync")
    public CompletableFuture<List<Document>> searchDocumentAsync(@RequestParam(value = "searchText") String searchText) throws InterruptedException {
        return documentService.searchAsync(searchText);
    }


    private Document convertToEntity(DocumentDto documentDtoDto) throws ParseException {
        Document document = modelMapper.map(documentDtoDto, Document.class);
        document.setDate(documentDtoDto.dateConverted());

        return document;
    }

}