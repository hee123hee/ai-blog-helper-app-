package com.example.bloghelper.dto;

import com.example.bloghelper.entity.Post;
import com.example.bloghelper.util.JsonConverter;
import com.fasterxml.jackson.core.type.TypeReference;

import java.time.LocalDateTime;
import java.util.List;

/**
 * PostResponse DTO는 클라이언트에게 게시글 정보를 응답하기 위한 데이터 전송 객체입니다.
 * Post 엔티티의 내부 필드를 읽기 전용 형태로 제공하며, JSON 응답으로 반환하기 적합한 형태로 변환합니다.
 *
 * @param id              게시글 식별자
 * @param title           게시글 제목
 * @param content         게시글 내용
 * @param status          게시글 상태(DRAFT, PUBLISHED)
 * @param keyword         게시글과 연관된 원본 키워드
 * @param relatedKeywords 연관 키워드 목록(엔티티에서는 JSON 문자열로 저장, DTO에서는 List로 변환)
 * @param createdAt       게시글 생성 시간
 * @param updatedAt       게시글 수정 시간
 */
public record PostResponse(
        Long id,
        String title,
        String content,
        Post.PostStatus status,
        String keyword,
        List<String> relatedKeywords,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    /**
     * Post 엔티티 객체를 PostResponse DTO로 변환하는 정적 팩토리 메서드입니다.
     * 엔티티에 JSON으로 저장된 relatedKeywords를 JsonConverter를 통해 List<String>으로 변환합니다.
     *
     * @param post DTO로 변환할 Post 엔티티
     * @return PostResponse DTO
     */
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getStatus(),
                post.getKeyword(),
                JsonConverter.fromJson(post.getRelatedKeywords(), new TypeReference<>() {
                }),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
