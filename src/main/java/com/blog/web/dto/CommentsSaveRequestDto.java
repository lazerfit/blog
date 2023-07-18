package com.blog.web.dto;

import com.blog.domain.comments.Comment;
import com.blog.domain.posts.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentsSaveRequestDto {

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

}
