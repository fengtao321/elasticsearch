package com.demo.elasticsearch.repository.impl;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.demo.elasticsearch.model.Document;
import com.demo.elasticsearch.config.PropsConfig;
import com.demo.elasticsearch.repository.DocumentRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.data.elasticsearch.core.SearchHit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders.match;

@RequiredArgsConstructor
@Repository
@Slf4j
public class DocumentRepositoryCustomImpl implements DocumentRepositoryCustom {

    private final ElasticsearchTemplate elasticsearchTemplate;

    private final ElasticsearchClient client;

    private final ElasticsearchAsyncClient asyncESClient;
    
    private final PropsConfig props;

    @Override
    public Optional<Document> findByTitleAuthor(String title, String author) {
        var criteria = QueryBuilders.bool(builder -> builder.must(
                match(queryAuthor -> queryAuthor.field("author").query(author)),
                match(queryTitle -> queryTitle.field("title").query(title))
        ));
        return elasticsearchTemplate.search(NativeQuery.builder().withQuery(criteria).build(), Document.class).stream().map(SearchHit::getContent).findFirst();
    }

    @Override
    public List<Document> search(String searchText) throws IOException {
        return getDocumentList(searchInclude(searchText));
    }

    @Override
    public CompletableFuture<List<Document>> searchAsync(String searchText) {
        return searchIncludeAsync(searchText).thenApply(r -> getDocumentList(r));
    }

    private List<Document> getDocumentList(SearchResponse<Document> searchResult) {
        List<Document> documentList = new ArrayList<>();
        List<Hit<Document>> hits = searchResult.hits().hits();
        hits.stream().map(hit->hit.source()).forEach(documentList::add);
        return documentList;
    }

    private SearchResponse<Document> searchInclude(String searchText) throws IOException {
        return client.search(s -> s
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
    }

    private CompletableFuture<SearchResponse<Document>> searchIncludeAsync(String searchText) {
        return asyncESClient.search(s -> s
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
    }
}
