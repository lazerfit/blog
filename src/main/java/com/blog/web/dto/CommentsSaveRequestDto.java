package com.blog.web.dto;

import com.blog.domain.comments.Comment;
import com.blog.domain.posts.Posts;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentsSaveRequestDto {

    private final String username;
    private final String content;
    private final Comment parentComment;
    private final Posts posts;

    public Comment toEntity() {
        return Comment.builder()
            .username(username)
            .content(content)
            .parent(parentComment)
            .posts(posts)
            .build();
    }

}
