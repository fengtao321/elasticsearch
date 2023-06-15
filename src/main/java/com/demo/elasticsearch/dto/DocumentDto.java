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
    public static final SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    @NotBlank
    private String title;

    @DocumentDate
    private String date;

    private String author;

    private String content;

    private String subject;

    @SneakyThrows
    public int dateConverted() {
        Date d = dateFormat.parse(this.date);
        return (int)(d.getTime())/1000;
    }

    public void setDate(Date date) {
        this.date = dateFormat.format(date);
    }

}
