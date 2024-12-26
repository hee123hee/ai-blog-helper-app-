package com.example.bloghelper.exception;

/**
 * ChatGPT 연동 과정에서 발생하는 예외를 처리하기 위한 사용자 정의 예외 클래스입니다.
 *
 * ChatGptError를 생성자에 주입받아 예외 메시지를 설정하며,
 * RuntimeException을 상속받아 unchecked exception으로 동작합니다.
 */
public class ChatGptException extends RuntimeException {
    private final ChatGptError error;

    /**
     * ChatGptException 생성자
     *
     * @param error ChatGptError 인터페이스의 구현체(ClientError 또는 ServerError)
     *              - ClientError이면 클라이언트 측 오류 메시지를,
     *              - ServerError이면 서버 측 오류 메시지를
     *              예외 메시지로 설정합니다.
     */
    public ChatGptException(ChatGptError error) {
        super(error instanceof ChatGptError.ClientError ce ? ce.message() :
                error instanceof ChatGptError.ServerError se ? se.message() :
                        "Unknown error");
        this.error = error;
    }
}
