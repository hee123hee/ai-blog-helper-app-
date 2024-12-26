package com.example.bloghelper.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class PostImprovementException extends RuntimeException {
    public PostImprovementException(String message, Exception e) {
        super(message, e);
    }
}
