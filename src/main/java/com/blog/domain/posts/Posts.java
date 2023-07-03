package com.blog.domain.posts;

import com.blog.domain.category.Category;
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

    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;

    @Builder
    public Posts(String title, String content, String author,Category category) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.category=category;
    }

    public PostsEditor.PostsEditorBuilder toEditor() {
        return PostsEditor.builder()
            .title(title)
            .content(content);
    }

    public void edit(PostsEditor postsEditor) {
        title=postsEditor.title();
        content=postsEditor.content();
    }
}
