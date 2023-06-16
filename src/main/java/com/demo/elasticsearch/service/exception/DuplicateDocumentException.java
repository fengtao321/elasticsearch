package com.demo.elasticsearch.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Duplicated Document")
public class DuplicateDocumentException extends Exception {

    public DuplicateDocumentException(String message) {
        super(message);
    }
}