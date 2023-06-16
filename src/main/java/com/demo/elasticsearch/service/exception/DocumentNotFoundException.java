package com.demo.elasticsearch.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Document Not Found")
public class DocumentNotFoundException extends Exception {

    public DocumentNotFoundException(String message) {
        super(message);
    }
}