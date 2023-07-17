package com.blog.web.dto;

import com.blog.web.form.CommentEditForm;
import lombok.Getter;

@Getter
public class CommentEditRequest {

    private final Long id;
    private final String content;

    public CommentEditRequest(CommentEditForm form) {
        this.id= form.getId();
        this.content=form.getContent();
    }

    public CommentEditRequest(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}
