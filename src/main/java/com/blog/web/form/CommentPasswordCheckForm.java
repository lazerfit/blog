package com.blog.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentPasswordCheckForm {

    @NotNull(message = "댓글 Id는 필수입니다.")
    private Long commentId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

}
