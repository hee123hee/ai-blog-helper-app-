package com.example.bloghelper.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUpdateRequest {

    @NotBlank(message = "제목은 필수입니다")
    // 제목 필드 검증: 비어있거나 공백일 수 없음.
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    // 내용 필드 검증: 비어있거나 공백일 수 없음.
    private String content;
}
