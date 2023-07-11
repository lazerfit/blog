package com.blog.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentForm {

    @NotBlank(message = "이름은 필수입니다.") String username;
    @NotBlank(message = "내용은 필수입니다.") String content;
    Long parentId;
    @NotNull(message = "게시글 ID 는 필수입니다.") Long postId;

    public void setParentId(Long parentId) {
        this.parentId = Objects.requireNonNullElse(parentId, 0L);
    }
}
