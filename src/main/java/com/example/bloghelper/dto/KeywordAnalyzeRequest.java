package com.example.bloghelper.dto;

import jakarta.validation.constraints.NotBlank;

public record KeywordAnalyzeRequest(
        @NotBlank(message = "키워드는 필수입니다")
        String keyword
) {}
