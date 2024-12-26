package com.example.bloghelper.controller;

import com.example.bloghelper.dto.KeywordAnalyzeRequest;
import com.example.bloghelper.dto.KeywordAnalyzeResponse;
import com.example.bloghelper.dto.PostCreateRequest;
import com.example.bloghelper.service.KeywordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/keywords")
@RequiredArgsConstructor
public class KeywordController {
    private final KeywordService keywordService;

    @PostMapping("/analyze")
    public Mono<ResponseEntity<KeywordAnalyzeResponse>> analyzeKeyword(
            @RequestBody @Valid PostCreateRequest request
    ) {
        return keywordService.analyzeKeyword(request.keyword())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

