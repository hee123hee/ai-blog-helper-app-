package com.example.bloghelper.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리 핸들러(Controller Advice) 클래스입니다.
 *
 * Spring MVC의 @RestControllerAdvice를 통해 전역적으로 발생하는 예외를 캐치하고,
 * 적절한 HTTP 응답을 반환할 수 있습니다.
 *
 * 여기서는 ChatGptException을 처리하여 문제 상황에 대한 HTTP 응답을 생성합니다.
 */
@RestControllerAdvice
@Slf4j // Lombok 어노테이션으로, 로깅을 위한 Logger 객체를 자동으로 생성합니다.
public class GlobalExceptionHandler {
    /**
     * ChatGptException 발생 시 이 메서드가 호출됩니다.
     *
     * @param e 처리할 ChatGptException 객체
     * @return SERVICE_UNAVAILABLE(503) 상태 코드와 함께 문제 세부 사항(ProblemDetail)을 담은 응답
     */
    @ExceptionHandler(ChatGptException.class)
    public ResponseEntity<ProblemDetail> handleChatGptException(ChatGptException e) {
        // 예외 발생 사실을 로그로 기록합니다.
        log.error("ChatGPT API 오류", e);

        // ProblemDetail을 통해 문제가 되는 상황을 상세히 기술합니다.
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                e.getMessage() // 예외 메시지를 상세 내용으로 사용
        );

        // 503 Service Unavailable 상태코드와 ProblemDetail을 바디로 하는 응답을 반환합니다.
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(problemDetail);
    }
}
