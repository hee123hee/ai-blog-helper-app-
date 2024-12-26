package com.example.bloghelper.service;

import com.example.bloghelper.dto.PostHistoryResponse;
import com.example.bloghelper.dto.PostImproveRequest;
import com.example.bloghelper.dto.PostImproveResponse;
import com.example.bloghelper.entity.Post;
import com.example.bloghelper.exception.PostNotFoundException;
import com.example.bloghelper.repository.PostRepository;
import com.example.bloghelper.util.JsonConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostImproveService {
    private final ChatGptService chatGptService;
    private final PostRepository postRepository;

    public Mono<PostImproveResponse> improvePost(Long postId, PostImproveRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("포스트를 찾을 수 없습니다: " + postId));

        return chatGptService.getCompletion(createImprovePrompt(post, request))
                .map(response -> parseImprovement(response))
                .map(improvement -> applyImprovement(post, improvement, request));
    }

    private String createImprovePrompt(Post post, PostImproveRequest request) {
        return """
                다음 블로그 포스트를 개선해주세요:
                제목: %s
                내용: %s

                개선 방향: %s
                추가 지시사항: %s

                다음 형식의 순수한 JSON으로 응답해주세요(다른형식 불가: md, xml 등...):
                {
                    "title": "개선된 제목",
                    "content": "개선된 내용",
                    "improvementReason": "개선 내용 설명"
                }
                """.formatted(
                post.getTitle(),
                post.getContent(),
                request.type().getDescription(),
                request.additionalInstructions() != null ? request.additionalInstructions() : "없음"
        );
    }

    private PostImprovement parseImprovement(String response) {
        return JsonConverter.fromJson(response, new TypeReference<PostImprovement>() {});
    }

    private PostImproveResponse applyImprovement(Post post, PostImprovement improvement,
                                                 PostImproveRequest request) {
        var history = post.improve(
                improvement.title(),
                improvement.content(),
                improvement.improvementReason()
        );
        postRepository.save(post);
        return PostImproveResponse.from(post, history);
    }

    @Transactional(readOnly = true)
    public List<PostHistoryResponse> getPostHistories(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("포스트를 찾을 수 없습니다: " + postId));

        return post.getHistories().stream()
                .map(PostHistoryResponse::from)
                .toList();
    }

}

record PostImprovement(String title, String content, String improvementReason) {
}

