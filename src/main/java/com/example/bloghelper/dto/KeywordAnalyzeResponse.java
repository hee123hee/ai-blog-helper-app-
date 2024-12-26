package com.example.bloghelper.dto;

import com.example.bloghelper.entity.Keyword;
import com.example.bloghelper.util.JsonConverter;
import com.fasterxml.jackson.core.type.TypeReference;

import java.time.LocalDateTime;
import java.util.List;

/**
 * KeywordAnalyzeResponse는 Keyword 엔티티의 분석 결과를 클라이언트에 전달하기 위한 DTO입니다.
 * <p>
 * Keyword 엔티티의 데이터를 읽기 전용 형태로 변환하여, JSON 응답 시 사용합니다.
 * originalKeyword(원본 키워드), relatedKeywords(연관 키워드), suggestedTopics(추천 주제),
 * analyzedAt(분석 시간) 정보를 포함합니다.
 * <p>
 * Record 타입을 사용해 불변 객체로 선언하였으며, 각 필드는 생성자에서 최종 값이 할당됩니다.
 *
 * @param id              키워드 식별자
 * @param originalKeyword 원본 키워드
 * @param relatedKeywords 연관 키워드 목록
 * @param suggestedTopics 추천 주제 목록
 * @param analyzedAt      분석 수행 시간
 */
public record KeywordAnalyzeResponse(
        Long id,
        String originalKeyword,
        List<String> relatedKeywords,
        List<String> suggestedTopics,
        LocalDateTime analyzedAt
) {

    /**
     * Keyword 엔티티 객체를 KeywordAnalyzeResponse DTO로 변환하는 정적 팩토리 메서드입니다.
     * 엔티티에 저장된 JSON 문자열을 JsonConverter를 통해 List<String> 형태로 변환합니다.
     *
     * @param keyword 변환 대상 Keyword 엔티티
     * @return KeywordAnalyzeResponse DTO
     */
    public static KeywordAnalyzeResponse from(Keyword keyword) {
        return new KeywordAnalyzeResponse(
                keyword.getId(),
                keyword.getOriginalKeyword(),
                JsonConverter.fromJson(keyword.getRelatedKeywords(), new TypeReference<List<String>>() {
                }),
                JsonConverter.fromJson(keyword.getSuggestedTopics(), new TypeReference<List<String>>() {
                }),
                keyword.getAnalyzedAt()
        );
    }
}
