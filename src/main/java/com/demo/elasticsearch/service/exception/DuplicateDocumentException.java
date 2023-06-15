package com.demo.elasticsearch.service.exception;

public class DuplicateDocumentException extends Exception {

    public DuplicateDocumentException(String message) {
        super(message);
    }
}