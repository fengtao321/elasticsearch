package com.demo.elasticsearch.service.exception;

public class DocumentNotFoundException extends Exception {

    public DocumentNotFoundException(String message) {
        super(message);
    }
}