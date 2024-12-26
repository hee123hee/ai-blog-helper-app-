package com.example.bloghelper.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

/**
 * WebClientConfig 클래스는 외부 API와 통신하기 위해 WebClient를 설정하는 빈(Bean)을 정의합니다.
 * 여기서는 ChatGPT API에 요청을 보내기 위한 WebClient를 생성합니다.
 */
@Configuration // 스프링 IoC 컨테이너에서 빈으로 등록하기 위한 클래스임을 표시합니다.
@RequiredArgsConstructor // Lombok 어노테이션으로, final 필드들에 대한 생성자를 자동 생성합니다.
public class WebClientConfig {

    // ChatGPT API 관련 설정 정보를 담고 있는 ChatGptConfig를 주입받습니다.
    private final ChatGptConfig chatGptConfig;

    /**
     * ChatGPT API와 통신하기 위한 WebClient 빈(Bean)을 생성하는 메서드입니다.
     *
     * @return ChatGPT API 호출에 사용할 WebClient 객체
     */
    @Bean
    public WebClient chatGptWebClient() {
        // WebClient.builder()를 통해 WebClient 인스턴스를 빌드합니다.
        return WebClient.builder()
                // ChatGPT API 기본 URL을 설정합니다.
                .baseUrl(chatGptConfig.getApiUrl())
                // 요청 시 기본으로 CONTENT_TYPE을 application/json으로 설정합니다.
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                // 인증을 위해 Authorization 헤더에 Bearer 토큰을 설정합니다.
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + chatGptConfig.getApiKey())
                // 응답 받을 수 있는 메모리 버퍼 사이즈를 늘려 대용량 응답에 대응합니다.
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build();
    }
}
