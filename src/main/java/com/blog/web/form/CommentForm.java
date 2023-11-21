package com.blog.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CommentForm {

    @Length(min = 1, max = 10, message = "작성자는 1~10글자로 입력해주세요.")
    @NotBlank(message = "이름은 필수입니다.")
    String username;

    @NotBlank(message = "내용은 필수입니다.") String content;
    Long parentId;
    @NotNull(message = "게시글 ID 는 필수입니다.") Long postId;
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Length(min = 4, max = 14, message = "비밀번호는 4~14글자로 입력해주세요.")
    String password;

    public void setParentId(Long parentId) {
        this.parentId = Objects.requireNonNullElse(parentId, 0L);
    }
}
