package com.blog.web.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter //Setter 열어두고 Dto 로 데이터 전송
public class EditPostsForm {

    @NotBlank(message = "제목은 필수입니다.") String title;
    @NotBlank(message = "내용은 필수입니다.") String content;

    public EditPostsForm(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
