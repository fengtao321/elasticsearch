package com.demo.elasticsearch.repository.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.demo.elasticsearch.model.Document;
import com.demo.elasticsearch.repository.DocumentRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.data.elasticsearch.core.SearchHit;

import java.util.Optional;

import static co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders.match;

@RequiredArgsConstructor
@Repository
public class DocumentRepositoryCustomImpl implements DocumentRepositoryCustom {

    private final ElasticsearchTemplate elasticsearchTemplate;
    @Override
    public Optional<Document> findByTitleAuthor(String title, String author) {
        var criteria = QueryBuilders.bool(builder -> builder.must(
                match(queryAuthor -> queryAuthor.field("author").query(author)),
                match(queryTitle -> queryTitle.field("title").query(title))
        ));
        return elasticsearchTemplate.search(NativeQuery.builder().withQuery(criteria).build(), Document.class).stream().map(SearchHit::getContent).findFirst();
    }
}
