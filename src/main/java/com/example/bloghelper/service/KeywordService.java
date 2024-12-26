package com.example.bloghelper.service;

import com.example.bloghelper.dto.KeywordAnalyzeResponse;
import com.example.bloghelper.entity.Keyword;
import com.example.bloghelper.exception.KeywordAnalysisException;
import com.example.bloghelper.repository.KeywordRepository;
import com.example.bloghelper.util.JsonConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * KeywordService 클래스는 사용자가 입력한 키워드에 대해 ChatGPT를 활용하여
 * 관련 키워드 및 추천 주제를 분석하고, 그 결과를 저장 및 반환하는 비즈니스 로직을 담당합니다.
 */
@Service
@Transactional // 메서드 실행 시 트랜잭션이 적용되며, 종료 시점에 커밋 또는 롤백합니다.
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성합니다.
@Slf4j // 로깅을 위한 Logger 객체를 자동으로 생성합니다.
public class KeywordService {
    private final ChatGptService chatGptService; // ChatGPT API 호출을 담당하는 서비스
    private final KeywordRepository keywordRepository; // Keyword 엔티티 저장소

    /**
     * 사용자가 입력한 키워드를 바탕으로 ChatGPT에 요청하여 관련 키워드와 추천 주제를 분석합니다.
     * 반환값은 비동기 처리(Mono)로, KeywordAnalyzeResponse DTO로 분석 결과를 반환합니다.
     *
     * @param keyword 사용자가 입력한 키워드
     * @return 분석 결과를 포함한 KeywordAnalyzeResponse를 Mono로 래핑한 객체
     */
    public Mono<KeywordAnalyzeResponse> analyzeKeyword(String keyword) {
        return chatGptService.getCompletion(createPrompt(keyword)) // ChatGPT에 프롬프트를 보내고 응답 수신
                .map(this::parseGptResponse) // 수신한 문자열 응답을 KeywordAnalysis 객체로 파싱
                .map(analysis -> saveKeywordAnalysis(keyword, analysis)) // 파싱한 결과를 데이터베이스에 저장하고 Keyword 엔티티 반환
                .map(KeywordAnalyzeResponse::from); // Keyword 엔티티를 KeywordAnalyzeResponse DTO로 변환
    }

    /**
     * ChatGPT에 전달할 프롬프트를 생성하는 메서드입니다.
     * 키워드와 관련된 블로그 주제를 JSON 형식으로 달라고 요청합니다.
     *
     * @param keyword 요청할 키워드
     * @return ChatGPT에 전달할 문자열 프롬프트
     */
    private String createPrompt(String keyword) {
        return """
                다음 키워드와 관련된 블로그 주제를 추천해주세요:
                키워드: %s

                다음 형식의 순수한 JSON으로 응답해주세요(다른 형식 불가 ex: md, xml 등):
                {
                    "relatedKeywords": ["연관 키워드1", "연관 키워드2", ...],
                    "suggestedTopics": ["추천 주제1", "추천 주제2", ...]
                }
                """.formatted(keyword);
    }

    /**
     * ChatGPT로부터 받은 응답 문자열을 KeywordAnalysis 레코드로 파싱합니다.
     *
     * @param response ChatGPT 응답 JSON 문자열
     * @return KeywordAnalysis(연관 키워드와 추천 주제 리스트를 담은 객체)
     */
    private KeywordAnalysis parseGptResponse(String response) {
        try {
            return JsonConverter.fromJson(response, new TypeReference<KeywordAnalysis>() {});
        } catch (KeywordAnalysisException e) {
            log.error("ChatGPT 응답 파싱 실패: {}", response, e);
            throw new KeywordAnalysisException("ChatGPT응답이 유효하지 않습니다.", e);
        }
    }

    /**
     * 파싱한 분석 결과(KeywordAnalysis)를 데이터베이스에 저장합니다.
     * Keyword 엔티티에 JSON 문자열로 연관 키워드와 추천 주제를 저장합니다.
     *
     * @param originalKeyword 사용자 입력 키워드
     * @param analysis        파싱한 분석 결과
     * @return 저장된 Keyword 엔티티 객체
     */
    private Keyword saveKeywordAnalysis(String originalKeyword, KeywordAnalysis analysis) {
        // Keyword 엔티티 생성 및 JSON 직렬화 후 저장
        Keyword keyword = Keyword.createFromAnalysis(
                originalKeyword,
                JsonConverter.toJson(analysis.relatedKeywords()),
                JsonConverter.toJson(analysis.suggestedTopics())
        );
        return keywordRepository.save(keyword);
    }
}

/**
 * KeywordAnalysis 레코드는 ChatGPT 응답을 매핑하기 위한 데이터 구조입니다.
 * relatedKeywords: 연관 키워드 목록
 * suggestedTopics: 추천 주제 목록
 */
record KeywordAnalysis(List<String> relatedKeywords, List<String> suggestedTopics) {
}
