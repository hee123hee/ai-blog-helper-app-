package com.example.bloghelper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * ChatGptRequest 클래스는 ChatGPT API에 요청을 보낼 때 사용할 데이터 구조를 정의합니다.
 * 이 클래스는 ChatGPT 모델에 전달할 메시지 목록, 모델명, temperature(창의성 정도),
 * 그리고 반환받을 토큰 수 제한 등의 파라미터들을 포함합니다.
 *
 * @param model       사용할 모델의 이름 (예: "gpt-4-turbo")
 * @param messages    모델에 전달할 메시지 목록
 * @param temperature 생성 결과의 창의성(랜덤성)을 조절하는 파라미터
 * @param maxTokens   응답에 사용할 최대 토큰 수 (max_completion_tokens로 매핑)
 */
public record ChatGptRequest(
        String model,
        List<Message> messages,
        Double temperature,
        @JsonProperty("max_completion_tokens") Integer maxTokens
) {

    /**
     * Message 클래스는 개별 메시지의 형식을 정의합니다.
     * role은 메시지를 전달하는 주체("user", "system", "assistant")를 의미하고,
     * content는 실제 메시지 내용을 담습니다.
     *
     * @param role    메시지를 주고받는 주체를 나타내는 문자열 ("user", "system", "assistant")
     * @param content 메시지 내용
     */
    public record Message(
            String role,
            String content
    ) {
        /**
         * 사용자가 보낸 메시지 형태를 편리하게 생성하기 위한 헬퍼 메서드입니다.
         *
         * @param content 사용자가 전송할 메시지 내용
         * @return "user" 역할을 가진 Message 객체를 반환합니다.
         */
        public static Message userMessage(String content) {
            return new Message("user", content);
        }
    }
}
