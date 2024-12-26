package com.example.bloghelper.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Keyword 엔티티 클래스입니다.
 * 사용자가 입력한 키워드, 해당 키워드의 연관 키워드, 추천 주제 등을 저장합니다.
 * 데이터베이스의 'keyword' 테이블과 매핑되어 키워드 분석 결과를 관리합니다.
 */
@Entity // 이 클래스가 JPA 엔티티임을 나타내며, 데이터베이스 테이블과 매핑됩니다.
@Getter // Lombok 어노테이션으로 필드에 대한 Getter 메서드를 자동으로 생성합니다.
public class Keyword {
    @Id // 이 필드를 엔티티의 기본 키(primary key)로 지정합니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 기본 키 값 자동 생성을 위해 데이터베이스의 IDENTITY 전략을 사용합니다.
    private Long id;

    @Column(nullable = false)
    // originalKeyword 컬럼은 null이 될 수 없습니다.
    private String originalKeyword;    // 사용자 입력 키워드

    @Column(columnDefinition = "TEXT")
    // TEXT 타입으로 DB에 저장하며, 길이 제한 없이 긴 문자열을 저장할 수 있습니다.
    private String relatedKeywords;    // 연관 키워드 (예: JSON 형식으로 리스트를 저장)

    @Column(columnDefinition = "TEXT")
    // TEXT 타입으로 DB에 저장합니다.
    private String suggestedTopics;    // 추천 주제 (예: JSON 형식으로 저장)

    // 키워드가 분석된 시간을 저장합니다.
    private LocalDateTime analyzedAt;

    /**
     * 엔티티가 최초로 persist(저장)되기 전에 호출되는 메서드입니다.
     *
     * @PrePersist 어노테이션으로 엔티티가 영속화되기 전에 자동으로 실행됩니다.
     * 여기서는 분석 시간을 현재 시간으로 설정합니다.
     */
    @PrePersist
    protected void onCreate() {
        analyzedAt = LocalDateTime.now();
    }

    /**
     * 정적 팩토리 메서드로, Keyword 엔티티를 쉽게 생성하기 위해 사용합니다.
     *
     * @param originalKeyword 사용자가 입력한 원본 키워드
     * @param relatedKeywords 연관 키워드 정보(JSON 형식)
     * @param suggestedTopics 추천 주제 정보(JSON 형식)
     * @return Keyword 엔티티 객체
     */
    public static Keyword createFromAnalysis(String originalKeyword, String relatedKeywords, String suggestedTopics) {
        Keyword keyword = new Keyword();
        keyword.originalKeyword = originalKeyword;
        keyword.relatedKeywords = relatedKeywords;
        keyword.suggestedTopics = suggestedTopics;
        return keyword;
    }
}
