package com.example.bloghelper.util;

import com.example.bloghelper.exception.KeywordAnalysisException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * JsonConverter 클래스는 JSON 문자열을 자바 객체로 변환하는 기능을 제공합니다.
 * <p>
 * 주로 키워드 분석 결과(연관 키워드, 추천 주제 등)를
 * JSON 문자열 형태로 저장했다가 다시 읽어올 때 사용됩니다.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonConverter {
    // Jackson 라이브러리의 ObjectMapper를 사용하여 JSON 변환을 수행합니다.
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 주어진 JSON 문자열을 지정한 타입의 자바 객체로 변환하는 메서드입니다.
     *
     * @param json          변환할 JSON 문자열
     * @param typeReference 변환 대상 타입 정보를 담는 TypeReference 객체
     * @param <T>           변환하고자 하는 결과 객체 타입
     * @return 변환된 자바 객체
     * @throws KeywordAnalysisException JSON 파싱 오류 발생 시 예외를 던집니다.
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            // JSON 문자열을 typeReference에 맞는 객체로 변환합니다.
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            // JSON 문자열을 파싱하는 과정에서 오류가 발생하면 KeywordAnalysisException 예외를 발생시킵니다.
            throw new KeywordAnalysisException("JSON 변환 실패: " + json, e);
        }
    }

    /**
     * 자바 객체를 JSON 문자열로 변환합니다.
     *
     * @param object 변환할 자바 객체
     * @return JSON 문자열
     * @throws KeywordAnalysisException JSON 생성 오류 발생 시 예외를 던집니다.
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new KeywordAnalysisException(
                    "객체를 JSON 문자열로 변환 실패: " + object, e
            );
        }
    }

}
