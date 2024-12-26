package com.example.bloghelper.dto;

import com.example.bloghelper.entity.PostHistory;

import java.time.LocalDateTime;

public record PostHistoryResponse(
        Long id,
        Integer version,
        String title,
        String content,
        String improvementReason,
        LocalDateTime createdAt
) {
    public static PostHistoryResponse from(PostHistory history) {
        return new PostHistoryResponse(
                history.getId(),
                history.getVersion(),
                history.getTitle(),
                history.getContent(),
                history.getImprovementReason(),
                history.getCreatedAt()
        );
    }
}
