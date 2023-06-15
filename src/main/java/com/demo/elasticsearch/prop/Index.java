package com.demo.elasticsearch.prop;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Index {
    private String name;

    private String id;

    private String title;

    private String date;

    private String author;

    private String content;

    private String subject;
}
