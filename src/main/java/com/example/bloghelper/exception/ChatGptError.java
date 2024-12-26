package com.example.bloghelper.exception;
/**
 * ChatGPT 연동 시 발생할 수 있는 오류 상황을 표현하기 위한 sealed 인터페이스입니다.
 * Sealed 인터페이스를 사용함으로써, 해당 인터페이스를 구현하는 클래스(여기서는 record)가 제한되어
 * 코드의 안정성과 가독성을 높일 수 있습니다.
 */
public sealed interface ChatGptError {
    /**
     * 클라이언트 측 오류를 나타내는 record입니다.
     * 예: 잘못된 요청 파라미터로 인한 오류 등.
     *
     * @param message 오류 상세 메시지
     */
    record ClientError(String message) implements ChatGptError {}

    /**
     * 서버 측 오류를 나타내는 record입니다.
     * 예: 외부 ChatGPT API 서버 장애나 시간 초과 등.
     *
     * @param message 오류 상세 메시지
     */
    record ServerError(String message) implements ChatGptError {}
}