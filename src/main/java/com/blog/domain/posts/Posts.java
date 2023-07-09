package com.blog.domain.posts;

import com.blog.domain.category.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BasetimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 10000, nullable = false)
    private String content;

    @Column(length = 500, nullable = false)
    private String tags;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;

    @Column(columnDefinition = "bigint default 0",nullable = false)
    private Long hit;

    @Builder
    public Posts(String title, String content, String tags, Category category, Long hit) {
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.category = category;
        this.hit = hit;
    }

    public void updateHit(Long hit) {
        this.hit=hit;
    }
}
