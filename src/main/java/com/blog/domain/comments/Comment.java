package com.blog.domain.comments;

import com.blog.domain.posts.BaseTimeEntity;
import com.blog.domain.posts.Post;
import com.blog.web.dto.comments.CommentsResponse;
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

@Getter
@NoArgsConstructor
@Entity
public class Comment extends BaseTimeEntity {

    /**
     * username 과 password 입력 후 댓글 달 수 있도록
     * username 과 password 알아야 댓글 수정/삭제 가능하도록
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false,length = 20)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false,length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id",referencedColumnName = "comment_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent",orphanRemoval = true)
    private List<Comment> child=new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Post post;


    @Builder
    public Comment(String username, String content, Comment parent, Post post, String password) {
        this.username = username;
        this.content = content;
        this.parent = parent;
        this.post = post;
        this.password=password;
    }

    public Comment(CommentsResponse responseDto) {
        this.id= responseDto.getId();
        this.username= responseDto.getUsername();
        this.content= responseDto.getContent();

    }

    public void edit(String content) {
        this.content=content;
    }
}
