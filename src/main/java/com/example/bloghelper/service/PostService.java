package com.example.bloghelper.service;

import com.example.bloghelper.dto.KeywordAnalyzeResponse;
import com.example.bloghelper.dto.PostCreateRequest;
import com.example.bloghelper.dto.PostResponse;
import com.example.bloghelper.entity.Post;
import com.example.bloghelper.exception.PostGenerationException;
import com.example.bloghelper.repository.PostRepository;
import com.example.bloghelper.util.JsonConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * PostService는 키워드 분석을 바탕으로 ChatGPT를 통해 포스트를 생성하고
 * 해당 결과를 데이터베이스에 저장한 뒤, 사용자에게 응답하는 역할을 합니다.
 */
@Service
@Transactional // 트랜잭션 범위 내에서 데이터베이스 작업을 처리
@RequiredArgsConstructor // Lombok 어노테이션: final 필드에 대한 생성자를 자동 생성
@Slf4j // Lombok 어노테이션: Logger 객체를 자동으로 생성
public class PostService {
    private final ChatGptService chatGptService; // ChatGPT API 연동 서비스
    private final KeywordService keywordService; // 키워드 분석 서비스
    private final PostRepository postRepository; // Post 엔티티 데이터베이스 액세스

    /**
     * 사용자가 전달한 키워드 기반으로:
     * 1. KeywordService를 통해 키워드 분석 수행
     * 2. ChatGPT를 통해 분석 결과를 바탕으로 초안 상태의 포스트 생성
     * 3. 데이터베이스에 생성된 포스트를 저장
     * 4. PostResponse DTO 형태로 응답 반환
     *
     * @param request 포스트 생성 요청 DTO (키워드 포함)
     * @return 생성된 포스트 정보(PostResponse)를 Mono로 반환
     */
    public Mono<PostResponse> createPostDraft(PostCreateRequest request) {
        KeywordAnalyzeResponse keywordAnalysis = keywordService.analyzeKeyword(request.keyword())
                .timeout(Duration.ofMinutes(2))
                .block();

        PostGenerationResponse generateContent = chatGptService.getCompletion(createPostPrompt(keywordAnalysis))
                .map(response -> {
                    log.info(response);
                    // ChatGPT의 JSON 응답을 PostGenerationResponse로 파싱
                    return JsonConverter.fromJson(response, new TypeReference<PostGenerationResponse>() {
                    });
                })
                .block();

        Post savedPost = createAndSavePost(generateContent, keywordAnalysis);

        PostResponse response = PostResponse.from(savedPost);

        return Mono.just(response);
    }



    /**
     * 키워드 분석 결과(relatedKeywords, originalKeyword)를 바탕으로
     * ChatGPT에 포스트 작성을 요청할 프롬프트를 생성합니다.
     *
     * @param keywordAnalysis 키워드 분석 결과
     * @return ChatGPT에 전달할 문자열 프롬프트
     */
    private String createPostPrompt(KeywordAnalyzeResponse keywordAnalysis) {
        return """
                다음 키워드와 관련된 블로그 포스트를 작성해주세요:
                주제 키워드: %s
                관련 키워드: %s

                다음 형식의 순수한 JSON으로 응답해주세요(마크다운 형식 X):
                {
                    "title": "블로그 포스트 제목",
                    "content": "블로그 포스트 내용 (마크다운 형식)"
                }

                작성 시 다음 사항을 고려해주세요:
                1. SEO를 고려한 제목 작성
                2. 명확한 문단 구분
                3. 읽기 쉬운 설명
                4. 전문적이고 신뢰할 수 있는 톤
                5. 관련 키워드를 자연스럽게 포함
                6. "content" 필드의 문자열에서 줄바꿈은 반드시 '\\n'로 표시해주세요.
                """.formatted(
                keywordAnalysis.originalKeyword(),
                String.join(", ", keywordAnalysis.relatedKeywords())
        );
    }

    /**
     * ChatGPT로부터 생성된 포스트 타이틀과 콘텐츠, 키워드 분석 결과를 바탕으로
     * 초안 상태의 Post 엔티티를 생성하고 데이터베이스에 저장합니다.
     *
     * @param generatedContent ChatGPT가 생성한 포스트 내용(타이틀, 내용)
     * @param keywordAnalysis  키워드 분석 결과(원본 키워드, 연관 키워드 등)
     * @return 저장된 Post 엔티티 객체
     * @throws PostGenerationException JSON 직렬화 실패 시 발생
     */
    private Post createAndSavePost(PostGenerationResponse generatedContent,
                                   KeywordAnalyzeResponse keywordAnalysis) {
        // relatedKeywords를 JSON 문자열로 변환 후, 초안 상태의 Post 생성
        var post = Post.createDraft(
                generatedContent.title(),
                generatedContent.content(),
                keywordAnalysis.originalKeyword(),
                JsonConverter.toJson(keywordAnalysis.relatedKeywords())
        );
        return postRepository.save(post);
    }
}

/**
 * PostGenerationResponse 레코드는 ChatGPT가 생성한 포스트의 제목과 내용을 담습니다.
 *
 * @param title   생성된 블로그 포스트 제목
 * @param content 생성된 블로그 포스트 내용 (마크다운 형식)
 */
record PostGenerationResponse(String title, String content) {
}
