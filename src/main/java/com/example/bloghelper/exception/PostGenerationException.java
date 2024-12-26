package com.example.bloghelper.exception;

public class PostGenerationException extends RuntimeException {
    public PostGenerationException(String message) {
        super(message);
    }

    public PostGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
