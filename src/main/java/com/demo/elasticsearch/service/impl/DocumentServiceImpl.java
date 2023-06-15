package com.demo.elasticsearch.service.impl;

import static co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders.match;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import com.demo.elasticsearch.dto.DocumentDto;
import com.demo.elasticsearch.model.Document;
import com.demo.elasticsearch.prop.ConfigProps;
import com.demo.elasticsearch.repository.DocumentRepository;
import com.demo.elasticsearch.service.DocumentService;
import com.demo.elasticsearch.service.exception.DocumentNotFoundException;
import com.demo.elasticsearch.service.exception.DuplicateDocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final ElasticsearchTemplate elasticsearchTemplate;

    private final ElasticsearchClient esClient;

    private final ElasticsearchAsyncClient asyncESClient;

    private final ConfigProps props;

    @Override
    public Optional<Document> getByTitle(String title) {
        return documentRepository.findByTitle(title);
    }

    @Override
    public List<Document> getAll() {
        List<Document> documents = new ArrayList<>();
        documentRepository.findAll()
                .forEach(documents::add);
        return documents;
    }

    @Override
    public List<Document> findByAuthor(String author) {
        return documentRepository.findByAuthor(author);
    }

    @Override
    public List<Document> findByTitleAndAuthor(String title, String author) {
        var criteria = QueryBuilders.bool(builder -> builder.must(
                match(queryAuthor -> queryAuthor.field("author").query(author)),
                match(queryTitle -> queryTitle.field("title").query(title))
        ));

        return elasticsearchTemplate.search(NativeQuery.builder().withQuery(criteria).build(), Document.class)
                .stream().map(SearchHit::getContent).toList();
    }

    @Override
    public Document create(Document document) throws DuplicateDocumentException {
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
    public Document update(String id, DocumentDto documentDto) throws DocumentNotFoundException, ParseException {
        Document oldDocument = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("There is not document associated with the given id"));
        oldDocument.setContent(documentDto.getContent());
        oldDocument.setAuthor(documentDto.getAuthor());
        oldDocument.setDate(documentDto.dateConverted());
        oldDocument.setTitle(documentDto.getTitle());
        oldDocument.setSubject(documentDto.getSubject());
        return documentRepository.save(oldDocument);
    }

    @Override
//    @Async("asyncExecutor")
    public CompletableFuture<List<Document>> searchAsync(String searchText) throws InterruptedException{
        log.info("Thread-1: " + Thread.currentThread().getName());
        var response = asyncESClient.search(s -> s
                        .index(props.getIndex().getName())
                        .query(q -> q
                                .multiMatch(
                                        t -> t.fields(props.getIndex().getTitle(), props.getIndex().getAuthor(),props.getIndex().getContent(), props.getIndex().getSubject())
                                                .query(searchText.toLowerCase())
                                                .fuzziness("AUTO")
                                                .minimumShouldMatch("2")
                                )
                        ),
                Document.class
        );
        log.info("Thread-2: " + Thread.currentThread().getName());
        return response.thenApply(r -> {
            log.info("Thread-3: " + Thread.currentThread().getName());
            List<Document> result = new ArrayList<>();
            List<Hit<Document>> hits = r.hits().hits();
            hits.stream().map(hit->hit.source()).forEach(result::add);
            return result;
        });
    }

    @Override
    public List<Document> search(String searchText) {
        List<Document> result = new ArrayList<>();

        try {
            SearchResponse<Document> response = esClient.search(s -> s
                            .index(props.getIndex().getName())
                            .query(q -> q
                                    .multiMatch(
                                            t -> t.fields(props.getIndex().getTitle(), props.getIndex().getAuthor(),props.getIndex().getContent(), props.getIndex().getSubject())
                                            .query(searchText.toLowerCase())
                                                    .fuzziness("AUTO")
                                                    .minimumShouldMatch("2")
                                    )
                            ),
                    Document.class
            );
            TotalHits total = response.hits().total();
            boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

            if (isExactResult) {
                log.info("There are " + total.value() + " results");
            } else {
                log.info("There are more than " + total.value() + " results");
            }

            List<Hit<Document>> hits = response.hits().hits();
            hits.stream().map(hit->hit.source()).forEach(result::add);
//            for (Hit<ObjectNode> hit: hits) {
//                ObjectNode document = hit.source();
//                result.add(document);
//                log.info("Found document " + document.getTitle() + ", author " + document.getAuthor());
//            }
        } catch (Exception ex){
            log.error("The exception was thrown in wildcardQuery method.", ex);
        }

        return result;
    }

}
