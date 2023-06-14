package com.demo.elasticsearch.service.exception;

public class DuplicateIsbnException extends Exception {

    public DuplicateIsbnException(String message) {
        super(message);
    }
}