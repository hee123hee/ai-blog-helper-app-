package com.example.bloghelper.controller;

import com.example.bloghelper.dto.*;
import com.example.bloghelper.service.PostImproveService;
import com.example.bloghelper.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostImproveService postImproveService;

    @PostMapping("/drafts")
    public Mono<ResponseEntity<PostResponse>> createDraft(@RequestBody @Valid PostCreateRequest request) {
        return postService.createPostDraft(request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/{postId}/improve")
    public Mono<ResponseEntity<PostImproveResponse>> improvePost(
            @PathVariable Long postId,
            @RequestBody @Valid PostImproveRequest request
    ) {
        return postImproveService.improvePost(postId, request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{postId}/histories")
    public ResponseEntity<List<PostHistoryResponse>> getPostHistories(@PathVariable Long postId) {
        return ResponseEntity.ok()
                .body(postImproveService.getPostHistories(postId));
    }
}