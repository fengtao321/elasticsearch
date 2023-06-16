package com.demo.elasticsearch.service.impl;

import static co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders.match;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.demo.elasticsearch.dto.DocumentDto;
import com.demo.elasticsearch.model.Document;
import com.demo.elasticsearch.prop.ConfigProps;
import com.demo.elasticsearch.repository.DocumentRepository;
import com.demo.elasticsearch.repository.DocumentRepositoryCustom;
import com.demo.elasticsearch.service.DocumentService;
import com.demo.elasticsearch.service.exception.DocumentNotFoundException;
import com.demo.elasticsearch.service.exception.DuplicateDocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final DocumentRepositoryCustom documentRepositoryCustom;

    private final ModelMapper modelMapper;

    @Override
    public Optional<Document> getByTitle(String title) {
        return documentRepository.findByTitle(title);
    }

    @Override
    public List<Document> getAll() {
        List<Document> documents = new ArrayList<>();
        documentRepository.findAll().forEach(documents::add);
        return documents;
    }

    @Override
    public Optional<Document> getByTitleAndAuthor(String title, String author) {
        return documentRepositoryCustom.findByTitleAuthor(title, author);
    }

    @Override
    public Document create(DocumentDto documentDto) throws DuplicateDocumentException {
        Document document = modelMapper.map(documentDto, Document.class);
        if (getByTitle(document.getTitle()).isEmpty()) {
            return documentRepository.save(document);
        }
        throw new DuplicateDocumentException(String.format("The provided document: %s already exists. Use update instead!", document.getTitle()));
    }

    @Override
    public void deleteById(String id) {
        documentRepository.deleteById(id);
    }

    @Override
    public Document update(String id, DocumentDto documentDto) throws DocumentNotFoundException {
        String oldDocumentId = documentRepository.findById(id).map(Document::getId)
                .orElseThrow(() -> new DocumentNotFoundException("There is not document associated with the given id"));
        Document document = modelMapper.map(documentDto, Document.class);
        document.setId(oldDocumentId);
        return documentRepository.save(document);
    }

    @Override
    public CompletableFuture<List<Document>> search(String searchText){
        return documentRepositoryCustom.searchAsync(searchText);
    }

    @Override
    public List<Document> searchBlocking(String searchText) throws IOException {
        return documentRepositoryCustom.search(searchText);
    }

}
