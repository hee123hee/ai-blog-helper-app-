package com.example.bloghelper.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class PostHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private Integer version;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String improvementReason;

    private LocalDateTime createdAt;

    public PostHistory(Post post, String improvementReason) {
        this.post = post;
        this.version = post.getVersion();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.improvementReason = improvementReason;
        this.createdAt = LocalDateTime.now();
    }
}

