package com.demo.elasticsearch.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.demo.elasticsearch.metadata.DocumentDate;
import com.demo.elasticsearch.model.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.SneakyThrows;

@Data
public class DocumentDto {

    @NotBlank
    private String title;

    @DocumentDate
    private Date date;

    private String author;

    private String content;

    private String subject;

}
