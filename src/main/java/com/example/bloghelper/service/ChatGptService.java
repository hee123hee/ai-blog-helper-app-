package com.example.bloghelper.service;

import com.example.bloghelper.config.ChatGptConfig;
import com.example.bloghelper.dto.ChatGptRequest;
import com.example.bloghelper.dto.ChatGptResponse;
import com.example.bloghelper.exception.ChatGptError;
import com.example.bloghelper.exception.ChatGptException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * ChatGptService는 ChatGPT API와 비동기로 통신하여 사용자의 프롬프트에 대한 응답(Completion)을 얻는 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor // Lombok 어노테이션으로 final 필드에 대한 생성자를 자동 생성합니다.
@Slf4j // Lombok 어노테이션으로 로깅을 위한 Logger 객체를 자동으로 생성합니다.
public class ChatGptService {
    // ChatGPT API 호출을 위한 WebClient 인스턴스입니다.
    private final WebClient chatGptWebClient;
    // ChatGPT API 호출에 필요한 설정 정보(API URL, 모델, 키 등)를 담고 있는 설정 객체입니다.
    private final ChatGptConfig chatGptConfig;

    /**
     * 사용자가 입력한 prompt(질문 또는 요청)에 대해 ChatGPT 모델로부터 응답을 받아오는 메서드입니다.
     * 비동기 방식으로 동작하며, Mono<String>으로 응답 내용 문자열을 반환합니다.
     *
     * @param prompt 사용자가 ChatGPT에게 묻고자 하는 질문이나 요청 문장
     * @return Mono<String> ChatGPT의 응답(Completion) 내용
     */
    public Mono<String> getCompletion(String prompt) {

        // ChatGPT API로 요청하기 위한 요청 객체를 생성합니다.
        // model: 사용할 모델명, messages: 메시지 목록, temperature: 응답 창의성 정도, maxTokens: 응답 최대 토큰 수
        ChatGptRequest request = new ChatGptRequest(
                chatGptConfig.getModel(),
                List.of(ChatGptRequest.Message.userMessage(prompt)),
                chatGptConfig.getTemperature(),
                chatGptConfig.getMaxCompletionTokens()
        );

        // WebClient를 이용해 ChatGPT API에 POST 요청을 보냅니다.
        return chatGptWebClient.post()
                .bodyValue(request) // 요청 바디에 ChatGptRequest DTO를 담습니다.
                .retrieve() // 서버 응답을 받아옵니다.
                // HTTP 상태 코드가 4xx(Client Error)일 경우의 예외 처리
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(
                                new ChatGptException(
                                        new ChatGptError.ClientError("ChatGPT API 클라이언트 오류")
                                )
                        )
                )
                // HTTP 상태 코드가 5xx(Server Error)일 경우의 예외 처리
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(
                                new ChatGptException(
                                        new ChatGptError.ServerError("ChatGPT API 서버 오류")
                                )
                        )
                )
                // 응답 바디를 ChatGptResponse 클래스로 변환합니다.
                .bodyToMono(ChatGptResponse.class)
                // ChatGptResponse에서 choices 목록의 첫 번째 메시지의 content를 추출합니다.
                .map(response -> response.choices().get(0).message().content())
                // 에러 발생 시 로그를 남깁니다.
                .doOnError(error -> log.error("ChatGPT API 호출 중 오류 발생", error));
    }
}
