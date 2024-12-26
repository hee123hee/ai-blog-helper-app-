package com.example.bloghelper.dto;


import com.example.bloghelper.entity.Post;
import com.example.bloghelper.entity.PostHistory;

import java.time.LocalDateTime;

public record PostImproveResponse(
        Long id,
        String title,
        String content,
        Integer version,
        String improvementReason,
        LocalDateTime improvedAt
) {
    public static PostImproveResponse from(Post post, PostHistory history) {
        return new PostImproveResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getVersion(),
                history.getImprovementReason(),
                history.getCreatedAt()
        );
    }
}
