package com.example.bloghelper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * ChatGptResponse 클래스는 ChatGPT API로부터 응답받는 JSON 구조를 매핑하기 위한 Record 클래스입니다.
 * OpenAI의 ChatGPT API 응답 형식을 참고하여 필드와 내부 레코드를 정의하였습니다.
 * <p>
 * API 응답의 일반적인 형식:
 * {
 * "id": "응답 ID",
 * "object": "응답 타입(예: 'chat.completion')",
 * "choices": [ ... ],  // 모델이 생성한 답변 목록
 * "usage": { ... }     // 토큰 사용량 정보
 * }
 *
 * @param id      응답을 식별하는 고유한 ID
 * @param object  응답 객체 타입(예: "chat.completion")
 * @param choices 모델이 생성한 답변 목록
 * @param usage   토큰 사용량 정보
 */
public record ChatGptResponse(
        String id,
        String object,
        List<Choice> choices,
        Usage usage
) {

    /**
     * Choice 클래스는 하나의 모델 응답 선택지를 나타냅니다.
     * ChatGPT API는 한 번의 요청에 대해 여러 개의 답변(Choice)을 반환할 수 있습니다.
     *
     * @param message      모델이 생성한 메시지(역할과 콘텐츠를 포함)
     * @param index        이 Choice가 몇 번째 선택지인지 나타내는 인덱스
     * @param finishReason 응답이 종료된 이유(예: "stop", "length" 등)
     */
    public record Choice(
            Message message,
            Integer index,
            @JsonProperty("finish_reason") String finishReason
    ) {
    }

    /**
     * Message 클래스는 개별 메시지 정보를 담습니다.
     * role은 메시지를 주고받는 주체("system", "user", "assistant")를 나타내며,
     * content는 실제 텍스트 내용을 담습니다.
     *
     * @param role    메시지의 주체("system", "user", "assistant")
     * @param content 메시지의 실제 내용
     */
    public record Message(
            String role,
            String content
    ) {
    }

    /**
     * Usage 클래스는 이번 요청/응답 과정에서 사용된 토큰 수를 나타냅니다.
     * ChatGPT는 텍스트를 처리하기 위해 토큰 단위로 계산하는데, prompt_tokens는 요청에 사용한 토큰 수,
     * completion_tokens는 응답 생성에 사용한 토큰 수, total_tokens는 둘을 합한 총 토큰 수를 의미합니다.
     *
     * @param promptTokens     요청(프롬프트)에 사용된 토큰 수
     * @param completionTokens 응답(완성)에 사용된 토큰 수
     * @param totalTokens      요청 + 응답 토큰 수의 합
     */
    public record Usage(
            @JsonProperty("prompt_tokens") Integer promptTokens,
            @JsonProperty("completion_tokens") Integer completionTokens,
            @JsonProperty("total_tokens") Integer totalTokens
    ) {
    }
}
