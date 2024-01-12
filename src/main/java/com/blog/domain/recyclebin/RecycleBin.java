package com.blog.domain.recyclebin;

import com.blog.domain.posts.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Entity
public class RecycleBin extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String title;
    @Column(length = 10000, nullable = false)
    private String content;
    private String category;
    private String tag;
    private Long views;
    @Column(length = 500)
    private String thumbnail;

    @Builder
    public RecycleBin(String title, String content, String category, String tag, Long views,
        String thumbnail) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.tag = tag;
        this.views = views;
        this.thumbnail = thumbnail;
    }
}
