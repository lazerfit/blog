package com.blog.web.dto.comments;

import com.blog.domain.comments.Comment;
import com.blog.domain.posts.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentsSaveRequest {

    private final String username;
    private final String content;
    private final Comment parentComment;
    private final Post post;
    private final String password;

    public Comment toEntity() {
        return Comment.builder()
            .username(username)
            .content(content)
            .parent(parentComment)
            .post(post)
            .password(password)
            .build();
    }

    @Builder
    public CommentsSaveRequest(String username, String content, Comment parentComment, Post post,
        String password) {
        this.username = username;
        this.content = content;
        this.parentComment = parentComment;
        this.post = post;
        this.password = password;
    }
}
