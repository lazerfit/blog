package com.blog.domain.posts;

import com.blog.domain.category.Category;
import com.blog.domain.comments.Comment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;
@Slf4j
@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 10000, nullable = false)
    private String content;

    @Column(length = 500, nullable = false)
    private String tag;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Long views;

    @OneToMany(orphanRemoval = true,mappedBy = "post")
    private List<Comment> comments=new ArrayList<>();

    @Builder
    public Post(String title, String content, String tag, Category category, Long views) {
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.category = category;
        this.views = views;
    }

    public void addViews(Long hit) {
        this.views +=hit;
    }
}
