package com.example.bloghelper.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // 스프링 부트 테스트 환경을 구동하여, 실제 Beans들이 주입되고 컨텍스트 내에서 테스트가 실행되도록 합니다.
class ChatGptServiceTest {
    @Autowired
    ChatGptService chatGptService;
    // ChatGptService 빈을 주입받습니다. 실제 애플리케이션에서 사용되는 WebClient와 ChatGptConfig를 통해
    // 실제 ChatGPT API에 요청을 보낼 수 있습니다(환경에 따라 Mocking 필요).

    @Test
    @DisplayName("ChatGPT API 호출 테스트")
        // 이 테스트 메서드는 ChatGptService의 getCompletion 메서드를 호출하여 실제 응답을 받아오는지 검증합니다.
    void getCompletionTest() {
        // given: 테스트에 필요한 초기 상태나 값 지정
        String prompt = "반가워 GPT!! 나와 함께 블로그 애플리케이션을 제작해보자!";

        // when: 테스트 대상 메서드 호출
        // block 메서드를 사용해 Mono를 동기적으로 변환하고 응답을 대기합니다.
        String response = chatGptService.getCompletion(prompt)
                .block();

        // then: 기대하는 결과 검증
        // 응답이 null이 아니어야 합니다.
        assertThat(response).isNotNull();

        // 응답을 콘솔에 출력하여 실제 응답 내용 확인(테스트 로그 용도)
        System.out.println("ChatGPT 응답: " + response);
    }
}
