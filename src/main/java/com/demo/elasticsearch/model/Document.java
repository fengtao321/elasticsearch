package com.demo.elasticsearch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "books")
@Data
public class Document {

    @Id
    private String id;

    private String title;

    private int date;

    private String author;

    private String content;

    private String subject;
}