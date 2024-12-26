package com.example.bloghelper.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration // 스프링 빈으로 등록하여 애플리케이션 구동 시 해당 클래스를 설정정보로 사용하게 합니다.
@ConfigurationProperties(prefix = "chatgpt")
// application-template.yml 파일 내에 "chatgpt"로 시작하는 프로퍼티들을
// 이 클래스의 필드에 매핑합니다. 예: chatgpt.api-key, chatgpt.api-url 등
@Getter
@Setter
public class ChatGptConfig {
    // ChatGPT API와 통신하기 위한 API 키를 저장합니다.
    private String apiKey;
    // ChatGPT API의 기본 URL을 저장합니다.
    private String apiUrl;
    // 사용할 모델(예: GPT-3.5, GPT-4 등)을 저장합니다.
    private String model;
    // 응답의 창의성 정도를 나타내는 temperature 파라미터 값입니다.
    private Double temperature;
    // 응답의 최대 토큰 길이를 설정하는 파라미터입니다.
    private Integer maxCompletionTokens;
}

