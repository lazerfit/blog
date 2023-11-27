package com.blog.web.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter //Setter 열어두고 Dto 로 데이터 전송
public class PostEditForm {

    @NotBlank(message = "제목은 필수입니다.") private String title;
    @NotBlank(message = "내용은 필수입니다.") private String content;
    @NotBlank(message = "카테고리 선택은 필수입니다.") private String categoryTitle;
    String tag;

    @Builder
    public PostEditForm(String title, String content,String categoryTitle, String tag) {
        this.title = title;
        this.content = content;
        this.categoryTitle = categoryTitle;
        this.tag=tag;
    }
}
