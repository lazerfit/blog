package com.blog.web.dto;

import com.blog.domain.comments.Comment;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class CommentsResponseDto {

    private final Long id;
    private final String username;
    private final String content;
    private final LocalDateTime generationTimeStamp;
    private final Long parentId;
    private List<CommentsResponseDto> child=new ArrayList<>();

    public CommentsResponseDto(Comment comment) {
        this.id= comment.getId();
        this.username=comment.getUsername();
        this.content=comment.getContent();
        this.generationTimeStamp =comment.getGenerationTimeStamp();
        this.parentId= checkNullAndSetDefaultValue(comment);
    }

    private static long checkNullAndSetDefaultValue(Comment comment) {
        return comment.getParent() != null ? comment.getParent().getId() : 0L;
    }

    @QueryProjection
    public CommentsResponseDto(Long id, String username, String content, LocalDateTime generationTimeStamp,
        Long parentId) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.generationTimeStamp = generationTimeStamp;
        this.parentId = parentId;
    }

    public void insertChildComment(List<CommentsResponseDto> child) {
        this.child=child;
    }
}
