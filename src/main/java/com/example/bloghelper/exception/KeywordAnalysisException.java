package com.example.bloghelper.exception;

public class KeywordAnalysisException extends RuntimeException {
    public KeywordAnalysisException(String message) {
        super(message);
    }

    public KeywordAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}

