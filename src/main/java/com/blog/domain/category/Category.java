package com.blog.domain.category;

import com.blog.domain.posts.Posts;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 30)
    private String title;

    @OneToMany(mappedBy = "category")
    private List<Posts> posts = new ArrayList<>();

    @Column(unique = true)
    private Long listOrder;

    @Builder
    public Category(String title, Long listOrder) {
        this.title = title;
        this.listOrder=listOrder;
    }
}

