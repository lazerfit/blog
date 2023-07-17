package com.blog.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentEditForm {

    @NotNull(message = "게시글 ID는 필수입니다.")
    private Long id;
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

}
